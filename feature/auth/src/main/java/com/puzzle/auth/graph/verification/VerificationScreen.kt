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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.puzzle.auth.graph.verification.contract.VerificationState
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.component.PieceSubCloseTopBar
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.navigation.AuthGraph
import com.puzzle.navigation.AuthGraphDest
import com.puzzle.navigation.NavigationEvent
import kotlinx.coroutines.delay

@Composable
internal fun VerificationRoute(
    viewModel: VerificationViewModel = mavericksViewModel()
) {
    val state by viewModel.collectAsState()

    VerificationScreen(
        state = state,
        navigate = {},
    )
}

@Composable
fun VerificationScreen(
    state: VerificationState,
    navigate: (NavigationEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    // 전화번호, 인증번호는 회전 등에서 상태 유지를 위해 rememberSaveable 사용
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var verificationNumber by rememberSaveable { mutableStateOf("") }

    // 타이머 상태를 별도 함수로 추출
    val timerState = rememberTimerState(
        totalSeconds = 300, // 5분
        onTimerFinish = {
            // 필요한 후처리가 있으면 여기서 처리
        }
    )

    // 인증번호 받기 버튼/재전송 버튼 텍스트
    // - 타이머가 한 번이라도 시작했으면 재전송 버튼으로, 아니면 받기 버튼으로
    // - 혹은 'isTimerRunning' 상태에 맞추어서만 바꿔도 됨
    val requestButtonLabel = if (timerState.hasStarted) "인증번호 재전송" else "인증번호 받기"

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
            onCloseClick = { /* 뒤로가기 혹은 close 동작 */ },
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

        // -----------------------------
        // (1) 전화번호 입력 영역
        // -----------------------------
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
            // 기본 입력 필드
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

            // 인증번호 받기 / 재전송 버튼
            PieceSolidButton(
                label = requestButtonLabel,
                onClick = {
                    // 인증번호 API 호출 후 타이머 시작
                    timerState.resendCode()
                },
                enabled = phoneNumber.isNotEmpty()
            )
        }

        // -----------------------------
        // (2) 인증번호 입력 & 타이머 노출
        // -----------------------------
        // 타이머가 0이 아니면 인증번호 입력 영역 노출
        if (timerState.remainingTime > 0) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "인증 번호",
                style = PieceTheme.typography.bodySM,
                color = PieceTheme.colors.dark3,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 인증번호 입력 필드 + 남은 시간
            BasicTextField(
                value = verificationNumber,
                onValueChange = { verificationNumber = it },
                textStyle = PieceTheme.typography.bodyMM,
                decorationBox = { innerTextField ->
                    // 인증번호 입력 내부에 남은 시간을 표시
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        innerTextField()

                        Text(
                            text = formatTime(timerState.remainingTime),
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
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "안전한 이용을 위해 타인과 절대 공유하지 마세요.",
                style = PieceTheme.typography.bodySM,
                color = PieceTheme.colors.dark3,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // -----------------------------
        // (3) 최종 인증 / 다음 버튼
        // -----------------------------
        PieceSolidButton(
            label = "다음",
            onClick = {
                // 실제 인증 로직 처리
                // 예: navigate(AuthGraphDest.SomeNextRoute)
            },
            enabled = verificationNumber.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))
    }
}

/**
 * [totalSeconds]만큼 카운트다운을 진행하는 타이머 상태를 기억하는 Composable.
 * 필요한 타이머 관련 로직을 캡슐화하여, UI 단에서는 상태만 받아서 사용.
 */
@Composable
fun rememberTimerState(
    totalSeconds: Int,
    onTimerFinish: () -> Unit = {},
): TimerState {
    // 남은 시간
    var remainingTime by rememberSaveable { mutableStateOf(totalSeconds) }

    // 타이머 동작 여부
    var isTimerRunning by rememberSaveable { mutableStateOf(false) }

    // 한 번이라도 타이머를 시작했는지 여부
    var hasStarted by rememberSaveable { mutableStateOf(false) }

    // 타이머가 동작 중이고, 남은 시간이 있을 때 1초마다 감소
    if (isTimerRunning && remainingTime > 0) {
        LaunchedEffect(remainingTime) {
            delay(1000L)
            remainingTime--
            if (remainingTime <= 0) {
                isTimerRunning = false
                onTimerFinish()
            }
        }
    }

    // 타이머 시작 함수
    fun startTimer() {
        remainingTime = totalSeconds
        isTimerRunning = true
        hasStarted = true
    }

    // 인증번호 재전송 함수
    fun resendCode() {
        // 예: API 호출 등 인증번호 재발급 로직
        startTimer()
    }

    return remember {
        TimerState(
            remainingTime = remainingTime,
            isTimerRunning = isTimerRunning,
            hasStarted = hasStarted,
            startTimer = ::startTimer,
            resendCode = ::resendCode,
        )
    }
}

/**
 * 타이머 관련 상태값을 담고 있는 자료 구조.
 * 상태는 람다로 보관해두고, 필요한 시점에만 읽기 위해 함수 형태로 둠.
 */
data class TimerState(
    val remainingTime: Int,
    val isTimerRunning: Boolean,
    val hasStarted: Boolean,
    val startTimer: () -> Unit,
    val resendCode: () -> Unit,
)

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
            state = VerificationState(),
            navigate = {},
        )
    }
}