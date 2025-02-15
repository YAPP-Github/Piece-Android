package com.puzzle.matching.graph.main.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.ColorFilter
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
internal fun MatchingWaitingScreen(
    isNotificationEnabled: Boolean,
    remainTime: String,
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
            textStyle = PieceTheme.typography.branding,
            titleColor = PieceTheme.colors.white,
            rightComponent = {
                if (isNotificationEnabled) {
                    Image(
                        painter = painterResource(R.drawable.ic_alarm_black),
                        contentDescription = "알람",
                        colorFilter = ColorFilter.tint(PieceTheme.colors.white),
                        modifier = Modifier.size(32.dp),
                    )
                }
            },
            modifier = Modifier.padding(bottom = 20.dp),
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(PieceTheme.colors.white.copy(alpha = 0.1f)),
        ) {
            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.precious_connection_start))
                    withStyle(style = SpanStyle(color = PieceTheme.colors.subDefault)) {
                        append(remainTime)
                    }
                    append(stringResource(R.string.time_remaining))
                },
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
                    append("진중한 만남을 위한\n")
                    withStyle(SpanStyle(color = PieceTheme.colors.primaryDefault)) {
                        append("매칭 조각")
                    }
                    append("이 곧 도착할 거예요!")
                },
                style = PieceTheme.typography.headingMSB,
                color = PieceTheme.colors.black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 20.dp, bottom = 8.dp),
            )

            Text(
                text = "매일 밤 10시에 매칭 조각이 도착해요\n생성한 프로필을 검토하며 기다려 주세요.",
                style = PieceTheme.typography.bodySM,
                color = PieceTheme.colors.dark3,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 34.dp),
            )

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(R.drawable.ic_onboarding_matching),
                contentDescription = null,
                modifier = Modifier
                    .size(260.dp)
                    .padding(bottom = 14.dp),
            )

            Spacer(modifier = Modifier.weight(1f))

            PieceSolidButton(
                label = stringResource(R.string.check_my_profile),
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
private fun PreviewMatchingWaitingScreen() {
    PieceTheme {
        MatchingWaitingScreen(
            isNotificationEnabled = true,
            onCheckMyProfileClick = {},
            remainTime = " 20:20:20 "
        )
    }
}
