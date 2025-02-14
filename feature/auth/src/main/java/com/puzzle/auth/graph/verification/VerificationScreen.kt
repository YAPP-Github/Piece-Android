package com.puzzle.auth.graph.verification

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.auth.graph.verification.contract.VerificationIntent
import com.puzzle.auth.graph.verification.contract.VerificationSideEffect
import com.puzzle.auth.graph.verification.contract.VerificationState
import com.puzzle.auth.graph.verification.contract.VerificationState.AuthCodeStatus
import com.puzzle.auth.graph.verification.contract.VerificationState.AuthCodeStatus.VERIFIED
import com.puzzle.common.ui.addFocusCleaner
import com.puzzle.common.ui.clickable
import com.puzzle.common.ui.repeatOnStarted
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.component.PieceSubCloseTopBar
import com.puzzle.designsystem.component.PieceTextInputDefault
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.navigation.AuthGraphDest
import com.puzzle.navigation.NavigationEvent

@Composable
internal fun VerificationRoute(
    viewModel: VerificationViewModel = mavericksViewModel()
) {
    val state by viewModel.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel) {
        lifecycleOwner.repeatOnStarted {
            viewModel.sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is VerificationSideEffect.RequestAuthCode -> viewModel.requestAuthCode(
                        sideEffect.phoneNumber
                    )

                    is VerificationSideEffect.VerifyAuthCode -> viewModel.verifyAuthCode(
                        phoneNumber = sideEffect.phoneNumber,
                        code = sideEffect.code,
                    )

                    is VerificationSideEffect.Navigate -> viewModel.navigationHelper
                        .navigate(sideEffect.navigationEvent)
                }
            }
        }
    }

    VerificationScreen(
        state = state,
        onRequestAuthCodeClick = { phoneNumber ->
            viewModel.onIntent(VerificationIntent.OnRequestAuthCodeClick(phoneNumber))
        },
        onVerifyClick = { phoneNumber, code ->
            viewModel.onIntent(
                VerificationIntent.OnVerifyClick(
                    phoneNumber = phoneNumber,
                    code = code,
                )
            )
        },
        navigate = { viewModel.onIntent(VerificationIntent.Navigate(it)) },
    )
}

@Composable
private fun VerificationScreen(
    state: VerificationState,
    onRequestAuthCodeClick: (String) -> Unit,
    onVerifyClick: (String, String) -> Unit,
    navigate: (NavigationEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(state.isAuthCodeRequested) {
        if (state.isAuthCodeRequested) {
            focusManager.moveFocus(FocusDirection.Down)
        }
    }

    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var authCode by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .addFocusCleaner(focusManager)
            .background(PieceTheme.colors.white)
            .padding(horizontal = 20.dp),
    ) {
        PieceSubCloseTopBar(
            title = "",
            onCloseClick = { navigate(NavigationEvent.NavigateUp) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
        )

        VerificationHeader(modifier = Modifier.padding(top = 20.dp))

        PhoneNumberBody(
            phoneNumber = phoneNumber,
            isValidPhoneNumber = state.isValidPhoneNumber,
            isAuthCodeRequested = state.isAuthCodeRequested,
            isAuthCodeVerified = state.authCodeStatus == VERIFIED,
            onPhoneNumberChanged = { phoneNumber = it },
            onRequestAuthCodeClick = onRequestAuthCodeClick,
            onClearClick = { phoneNumber = "" },
            modifier = Modifier.padding(top = 68.dp)
        )

        if (state.isAuthCodeRequested) {
            AuthCodeBody(
                authCode = authCode,
                remainingTimeInSec = state.formattedRemainingTimeInSec,
                authCodeStatus = state.authCodeStatus,
                onAuthCodeChanged = { authCode = it },
                onVerifyClick = {
                    keyboardController?.hide()
                    onVerifyClick(phoneNumber, authCode)
                },
                modifier = Modifier.padding(top = 32.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        PieceSolidButton(
            label = stringResource(R.string.confirm),
            onClick = { navigate(NavigationEvent.NavigateTo(AuthGraphDest.SignUpRoute)) },
            enabled = state.authCodeStatus == VERIFIED,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
        )
    }
}

@Composable
private fun VerificationHeader(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(top = 20.dp)) {
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = PieceTheme.colors.primaryDefault)) {
                    append("휴대폰 번호")
                }
                append("로\n인증을 진행해 주세요")
            },
            style = PieceTheme.typography.headingLSB,
            color = PieceTheme.colors.black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
        )

        Text(
            text = stringResource(R.string.verification_subtitle),
            style = PieceTheme.typography.bodySM,
            color = PieceTheme.colors.dark3,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun AuthCodeBody(
    authCode: String,
    remainingTimeInSec: String,
    authCodeStatus: AuthCodeStatus,
    onAuthCodeChanged: (String) -> Unit,
    onVerifyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var lastDoneTime by remember { mutableLongStateOf(0L) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val authCodeStatusColor = when (authCodeStatus) {
        AuthCodeStatus.INIT -> PieceTheme.colors.dark3
        VERIFIED -> PieceTheme.colors.primaryDefault
        AuthCodeStatus.INVALID -> PieceTheme.colors.subDefault
        AuthCodeStatus.TIME_EXPIRED -> PieceTheme.colors.subDefault
    }

    val isVerifyButtonEnabled =
        authCodeStatus == AuthCodeStatus.INIT || authCodeStatus == AuthCodeStatus.INVALID

    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.verification_verifiaction_code),
            style = PieceTheme.typography.bodySM,
            color = PieceTheme.colors.dark3,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        ) {
            BasicTextField(
                value = authCode,
                onValueChange = onAuthCodeChanged,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        val currentTime = System.currentTimeMillis()
                        if (currentTime - lastDoneTime >= 1000L) {
                            keyboardController?.hide()
                            onVerifyClick()
                            lastDoneTime = currentTime
                        }
                    }
                ),
                textStyle = PieceTheme.typography.bodyMM,
                decorationBox = { innerTextField ->
                    Box {
                        innerTextField()

                        Text(
                            text = remainingTimeInSec,
                            style = PieceTheme.typography.bodySM,
                            color = PieceTheme.colors.primaryDefault,
                            modifier = Modifier.align(Alignment.CenterEnd),
                        )
                    }
                },
                modifier = Modifier
                    .height(52.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(PieceTheme.colors.light3)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
                    .weight(1f),
            )

            PieceSolidButton(
                label = stringResource(R.string.confirm),
                onClick = onVerifyClick,
                enabled = isVerifyButtonEnabled,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Text(
            text = stringResource(authCodeStatus.displayResId),
            style = PieceTheme.typography.bodySM,
            color = authCodeStatusColor,
        )
    }
}

@Composable
private fun PhoneNumberBody(
    phoneNumber: String,
    isAuthCodeRequested: Boolean,
    isValidPhoneNumber: Boolean,
    isAuthCodeVerified: Boolean,
    onPhoneNumberChanged: (String) -> Unit,
    onRequestAuthCodeClick: (String) -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val requestButtonLabel =
        if (isAuthCodeRequested) stringResource(R.string.verification_resend) else stringResource(R.string.verification_request)

    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.verification_phone_number),
            style = PieceTheme.typography.bodySM,
            color = PieceTheme.colors.dark3,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        ) {
            PieceTextInputDefault(
                value = phoneNumber,
                keyboardType = KeyboardType.Phone,
                onDone = {
                    if (phoneNumber.isNotEmpty()) {
                        onRequestAuthCodeClick(phoneNumber)
                    }
                },
                onValueChange = onPhoneNumberChanged,
                rightComponent = {
                    if (phoneNumber.isNotEmpty()) {
                        Image(
                            painter = painterResource(R.drawable.ic_delete_circle),
                            contentDescription = null,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { onClearClick() },
                        )
                    }
                },
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .weight(1f)
            )

            PieceSolidButton(
                label = requestButtonLabel,
                onClick = { onRequestAuthCodeClick(phoneNumber) },
                enabled = phoneNumber.isNotEmpty() && !isAuthCodeVerified,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Text(
            text = if (!isValidPhoneNumber) {
                stringResource(R.string.verification_invalid_phone_number)
            } else {
                stringResource(id = R.string.verification_phone_number_hint)
            },
            style = PieceTheme.typography.bodySM,
            color = if (!isValidPhoneNumber) {
                PieceTheme.colors.subDefault
            } else {
                PieceTheme.colors.dark3
            },
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Preview
@Composable
fun PreviewVerificationScreen() {
    PieceTheme {
        VerificationScreen(
            state = VerificationState(
                isAuthCodeRequested = true,
                remainingTimeInSec = 299,
                authCodeStatus = AuthCodeStatus.INIT,
            ),
            navigate = {},
            onRequestAuthCodeClick = {},
            onVerifyClick = { _, _ -> },
        )
    }
}
