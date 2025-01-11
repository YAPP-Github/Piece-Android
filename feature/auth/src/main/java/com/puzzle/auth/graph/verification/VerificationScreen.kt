package com.puzzle.auth.graph.verification

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.auth.graph.verification.contract.VerificationIntent
import com.puzzle.auth.graph.verification.contract.VerificationSideEffect
import com.puzzle.auth.graph.verification.contract.VerificationState
import com.puzzle.auth.graph.verification.contract.VerificationState.AuthCodeStatus
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.component.PieceSubCloseTopBar
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.navigation.AuthGraph
import com.puzzle.navigation.AuthGraphDest
import com.puzzle.navigation.NavigationEvent

@Composable
internal fun VerificationRoute(
    viewModel: VerificationViewModel = mavericksViewModel()
) {
    val state by viewModel.collectAsState()

    VerificationScreen(state = state, navigate = {
        viewModel.onSideEffect(VerificationSideEffect.Navigate(it))
    }, onRequestAuthCodeClick = { phoneNumber ->
        viewModel.onIntent(VerificationIntent.OnRequestAuthCodeClick(phoneNumber))
    }, onVerifyClick = { code ->
        viewModel.onIntent(VerificationIntent.OnVerifyClick(code))
    }, onNextClick = {
        viewModel.onIntent(VerificationIntent.OnNextClick)
    })
}

@Composable
private fun VerificationScreen(
    state: VerificationState,
    onRequestAuthCodeClick: (String) -> Unit,
    onVerifyClick: (String) -> Unit,
    onNextClick: () -> Unit,
    navigate: (NavigationEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PieceTheme.colors.white)
            .padding(horizontal = 20.dp)
            .clickable {
                navigate(
                    NavigationEvent.NavigateTo(
                        route = AuthGraphDest.RegistrationRoute,
                        popUpTo = AuthGraph,
                    )
                )
            },
    ) {
        PieceSubCloseTopBar(
            title = "",
            onCloseClick = {
                navigate(NavigationEvent.NavigateUp)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
        )

        VerificationHeader(
            modifier = Modifier.padding(top = 20.dp)
        )

        PhoneNumberBody(
            isValidPhoneNumber = state.isValidPhoneNumber,
            isAuthCodeRequested = state.isAuthCodeRequested,
            onRequestAuthCodeClick = onRequestAuthCodeClick,
            modifier = Modifier.padding(top = 68.dp)
        )

        if (state.isAuthCodeRequested) {
            AuthCodeBody(
                remainingTimeInSec = state.remainingTimeInSec,
                authCodeStatus = state.authCodeStatus,
                onVerifyClick = onVerifyClick,
                modifier = Modifier.padding(top = 32.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        PieceSolidButton(
            label = stringResource(R.string.verification_submit),
            onClick = {
                onNextClick()
            },
            enabled = state.isVerified,
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
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(12.dp))

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
    remainingTimeInSec: Int,
    authCodeStatus: AuthCodeStatus,
    onVerifyClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var authCode by rememberSaveable { mutableStateOf("") }

    val (authCodeStatusMessage, authgCodeStatusColor) = when (authCodeStatus) {
        AuthCodeStatus.DO_NOT_SHARE -> stringResource(R.string.verification_do_not_share) to PieceTheme.colors.dark3

        AuthCodeStatus.VERIFIED -> stringResource(R.string.verification_verified) to PieceTheme.colors.primaryDefault

        AuthCodeStatus.INVALID -> stringResource(R.string.verification_invalid_code) to PieceTheme.colors.subDefault

        AuthCodeStatus.TIME_EXPIRED -> stringResource(R.string.verification_time_expired) to PieceTheme.colors.subDefault
    }

    val isVerifyButtonEnabled =
        authCodeStatus == AuthCodeStatus.DO_NOT_SHARE || authCodeStatus == AuthCodeStatus.INVALID

    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.verification_verifiaction_code),
            style = PieceTheme.typography.bodySM,
            color = PieceTheme.colors.dark3,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicTextField(
                value = authCode,
                onValueChange = { authCode = it },
                textStyle = PieceTheme.typography.bodyMM,
                decorationBox = { innerTextField ->
                    Box {
                        innerTextField()

                        Text(
                            text = formatTime(remainingTimeInSec),
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
                    .padding(
                        horizontal = 16.dp,
                        vertical = 14.dp,
                    )
                    .weight(1f),
            )

            Spacer(modifier = Modifier.width(8.dp))

            PieceSolidButton(
                label = stringResource(R.string.verification_submit),
                onClick = {
                    onVerifyClick(authCode)
                },
                enabled = isVerifyButtonEnabled,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = authCodeStatusMessage,
            style = PieceTheme.typography.bodySM,
            color = authgCodeStatusColor,
        )
    }
}

@Composable
private fun PhoneNumberBody(
    isAuthCodeRequested: Boolean,
    isValidPhoneNumber: Boolean,
    onRequestAuthCodeClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var phoneNumber by rememberSaveable { mutableStateOf("") }

    val requestButtonLabel =
        if (isAuthCodeRequested) stringResource(R.string.verification_resend) else stringResource(R.string.verification_request)

    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.verification_phone_number),
            style = PieceTheme.typography.bodySM,
            color = PieceTheme.colors.dark3,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                textStyle = PieceTheme.typography.bodyMM,
                modifier = Modifier
                    .height(52.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(PieceTheme.colors.light3)
                    .padding(
                        horizontal = 16.dp,
                        vertical = 14.dp,
                    )
                    .weight(1f),
            )

            Spacer(modifier = Modifier.width(8.dp))

            PieceSolidButton(
                label = requestButtonLabel,
                onClick = {
                    onRequestAuthCodeClick(phoneNumber)
                },
                enabled = phoneNumber.isNotEmpty(),
            )
        }

        if (!isValidPhoneNumber) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.verification_invalid_phone_number),
                style = PieceTheme.typography.bodySM,
                color = PieceTheme.colors.subDefault,
            )
        }
    }
}

/**
 * 초 단위 [seconds]를 "mm:ss" 형태 문자열로 변환하는 함수
 */
@SuppressLint("DefaultLocale")
fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return String.format("%02d:%02d", minutes, secs)
}

@Preview
@Composable
fun PreviewVerificationScreen() {
    PieceTheme {
        VerificationScreen(
            state = VerificationState(
                isAuthCodeRequested = true,
                remainingTimeInSec = 299,
                isVerified = true,
                authCodeStatus = AuthCodeStatus.DO_NOT_SHARE,
            ),
            navigate = {},
            onRequestAuthCodeClick = {},
            onVerifyClick = {},
            onNextClick = {},
        )
    }
}