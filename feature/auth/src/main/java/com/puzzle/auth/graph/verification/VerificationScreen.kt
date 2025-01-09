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
import com.puzzle.auth.graph.verification.contract.VerificationState.VerificationCodeStatus
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

    VerificationScreen(
        state = state,
        navigate = {
            viewModel.onSideEffect(VerificationSideEffect.Navigate(it))
        },
        onRequestVerificationCodeClick = {
            viewModel.onIntent(VerificationIntent.RequestVerificationCode)
        },
        onVerifyClick = { code ->
            viewModel.onIntent(VerificationIntent.VerifyCode(code))
        },
        onNextClick = {
            viewModel.onIntent(VerificationIntent.CompleteVerification)
        }
    )
}

@Composable
private fun VerificationScreen(
    state: VerificationState,
    onRequestVerificationCodeClick: () -> Unit,
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

        Spacer(modifier = Modifier.height(20.dp))

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
            text = "신뢰도 높은 매칭과 안전한 커뮤니티를 위해 \n" +
                    "휴대폰 번호로 인증해 주세요.",
            style = PieceTheme.typography.bodySM,
            color = PieceTheme.colors.dark3,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(70.dp))

        PhoneNumberBody(
            hasStarted = state.hasStarted,
            onRequestVerificationCodeClick = onRequestVerificationCodeClick
        )

        if (state.hasStarted) {
            Spacer(modifier = Modifier.height(32.dp))

            VerificationCodeBody(
                remainingTimeInSec = state.remainingTimeInSec,
                verificationCodeStatus = state.verificationCodeStatus,
                onVerifyClick = onVerifyClick,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        PieceSolidButton(
            label = "다음",
            onClick = {
                onNextClick()
            },
            enabled = state.isVerified,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun VerificationCodeBody(
    remainingTimeInSec: Int,
    verificationCodeStatus: VerificationCodeStatus,
    onVerifyClick: (String) -> Unit,
) {
    var verificationCode by rememberSaveable { mutableStateOf("") }

    val (verificationCodeStatusMessage, verificationCodeStatusColor) =
        when (verificationCodeStatus) {
            VerificationCodeStatus.DO_NOT_SHARE ->
                "어떤 경우에도 타인에게 공유하지 마세요" to PieceTheme.colors.dark3

            VerificationCodeStatus.VERIFIED ->
                "전화번호 인증을 완료했어요" to PieceTheme.colors.primaryDefault

            VerificationCodeStatus.INVALID ->
                "올바른 인증번호가 아니에요" to PieceTheme.colors.subDefault

            VerificationCodeStatus.TIME_EXPIRED ->
                "유효시간이 지났어요! ‘인증번호 재전송’을 눌러주세요" to PieceTheme.colors.subDefault
        }

    val isVerifyButtonEnabled =
        verificationCodeStatus == VerificationCodeStatus.DO_NOT_SHARE ||
                verificationCodeStatus == VerificationCodeStatus.INVALID

    Text(
        text = "인증 번호",
        style = PieceTheme.typography.bodySM,
        color = PieceTheme.colors.dark3,
    )

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicTextField(
            value = verificationCode,
            onValueChange = { verificationCode = it },
            textStyle = PieceTheme.typography.bodyMM,
            decorationBox = { innerTextField ->
                Box {
                    innerTextField()

                    Text(
                        text = formatTime(remainingTimeInSec),
                        style = PieceTheme.typography.bodySM,
                        color = PieceTheme.colors.primaryDefault,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
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
            label = "확인",
            onClick = {
                onVerifyClick(verificationCode)
            },
            enabled = isVerifyButtonEnabled,
        )
    }

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = verificationCodeStatusMessage,
        style = PieceTheme.typography.bodySM,
        color = verificationCodeStatusColor,
    )
}

@Composable
private fun PhoneNumberBody(
    hasStarted: Boolean,
    onRequestVerificationCodeClick: () -> Unit
) {
    var phoneNumber by rememberSaveable { mutableStateOf("") }

    val requestButtonLabel = if (hasStarted) "인증번호 재전송" else "인증번호 받기"

    Text(
        text = "휴대폰 번호",
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
                onRequestVerificationCodeClick()
            },
            enabled = phoneNumber.isNotEmpty()
        )
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
                hasStarted = true,
                remainingTimeInSec = 299,
                isVerified = true,
                verificationCodeStatus = VerificationState.VerificationCodeStatus.DO_NOT_SHARE,
            ),
            navigate = {},
            onRequestVerificationCodeClick = {},
            onVerifyClick = {},
            onNextClick = {},
        )
    }
}