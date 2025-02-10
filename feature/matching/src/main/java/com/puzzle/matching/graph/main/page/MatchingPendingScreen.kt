package com.puzzle.matching.graph.main.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceMainTopBar
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.foundation.PieceTheme

@Composable
internal fun MatchingPendingScreen(
    onCheckMyProfileClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PieceTheme.colors.black)
            .padding(horizontal = 20.dp),
    ) {
        PieceMainTopBar(
            title = stringResource(R.string.matching_title),
            titleColor = PieceTheme.colors.white,
            rightComponent = {
                Image(
                    painter = painterResource(R.drawable.ic_alarm),
                    contentDescription = "",
                    modifier = Modifier.size(32.dp),
                )
            },
            modifier = Modifier.padding(vertical = 14.dp),
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(PieceTheme.colors.white.copy(alpha = 0.1f)),
        ) {
            Text(
                text = "심사가 완료되면 소중한 인연이 공개됩니다.",
                style = PieceTheme.typography.bodySM,
                color = PieceTheme.colors.light1,
                modifier = Modifier.padding(vertical = 12.dp),
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(top = 8.dp, bottom = 30.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(PieceTheme.colors.white)
                .padding(20.dp),
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = PieceTheme.colors.primaryDefault)) {
                        append("진중한 만남")
                    }
                    append("을 이어가기 위해\n프로필을 살펴보고 있어요.")
                },
                style = PieceTheme.typography.headingMSB,
                color = PieceTheme.colors.black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 20.dp, bottom = 8.dp),
            )

            Text(
                text = "작성 후 24시간 이내에 심사가 진행됩니다.\n생성한 프로필을 검토하며 기다려 주세요.",
                style = PieceTheme.typography.bodySM,
                color = PieceTheme.colors.dark3,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 34.dp),
            )

            Image(
                painter = painterResource(R.drawable.ic_user_pending_home),
                contentDescription = null,
                modifier = Modifier
                    .size(240.dp)
                    .padding(bottom = 14.dp),
            )

            PieceSolidButton(
                label = "내 프로필 확인하기",
                onClick = onCheckMyProfileClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
            )
        }
    }
}


@Preview
@Composable
private fun PreviewMatchingPendingScreen() {
    PieceTheme {
        MatchingPendingScreen(
            onCheckMyProfileClick = {},
        )
    }
}
