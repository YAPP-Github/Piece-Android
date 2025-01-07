package com.puzzle.auth.graph.verification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.puzzle.auth.graph.verification.contract.VerificationState
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
        navigate = {},
    )
}

@Composable
private fun VerificationScreen(
    state: VerificationState,
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
            onCloseClick = { },
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

        Text(
            text = "휴대폰 번호",
            style = PieceTheme.typography.bodySM,
            color = PieceTheme.colors.dark3,
        )

        Spacer(modifier = Modifier.height(8.dp))

        var phoneNumber by rememberSaveable { mutableStateOf("") }

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
                label = "인증 번호 받기",
                onClick = {},
                enabled = true,
            )
        }

        if (true) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "인증 번호",
                style = PieceTheme.typography.bodySM,
                color = PieceTheme.colors.dark3,
            )

            Spacer(modifier = Modifier.height(8.dp))

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

        PieceSolidButton(
            label = "인증 번호 받기",
            onClick = {},
            enabled = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))
    }
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