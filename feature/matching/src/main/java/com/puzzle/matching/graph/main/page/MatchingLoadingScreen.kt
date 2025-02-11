package com.puzzle.matching.graph.main.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceMainTopBar
import com.puzzle.designsystem.foundation.PieceTheme

@Composable
internal fun MatchingLoadingScreen() {
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
                Image(
                    painter = painterResource(R.drawable.ic_alarm_black),
                    contentDescription = "알람",
                    colorFilter = ColorFilter.tint(PieceTheme.colors.white),
                    modifier = Modifier.size(32.dp),
                )
            },
            modifier = Modifier.padding(bottom = 20.dp),
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(PieceTheme.colors.white.copy(alpha = 0.1f)),
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(top = 8.dp, bottom = 30.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(PieceTheme.colors.white)
                .padding(20.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Spacer(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(PieceTheme.colors.light2)
                )

                Spacer(
                    modifier = Modifier
                        .size(width = 40.dp, height = 20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(PieceTheme.colors.light2)
                )

                Spacer(
                    modifier = Modifier
                        .size(width = 160.dp, height = 20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(PieceTheme.colors.light2)
                )
            }

            Column {
                Spacer(
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .fillMaxWidth()
                        .height(64.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(PieceTheme.colors.light2)
                )

                Spacer(
                    modifier = Modifier
                        .size(width = 180.dp, height = 24.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(PieceTheme.colors.light2)
                )
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = PieceTheme.colors.light2,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                Spacer(
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .size(width = 140.dp, height = 24.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(PieceTheme.colors.light2),
                )

                Spacer(
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .fillMaxWidth()
                        .height(36.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(PieceTheme.colors.light2),
                )

                Spacer(
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .fillMaxWidth()
                        .height(36.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(PieceTheme.colors.light2),
                )

                Spacer(
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .size(width = 180.dp, height = 24.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(PieceTheme.colors.light2),
                )

                Spacer(
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .fillMaxWidth()
                        .height(36.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(PieceTheme.colors.light2),
                )

                Spacer(
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .fillMaxWidth()
                        .height(36.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(PieceTheme.colors.light2),
                )

                Spacer(
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .size(width = 180.dp, height = 24.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(PieceTheme.colors.light2),
                )

                Spacer(
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .fillMaxWidth()
                        .height(36.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(PieceTheme.colors.light2),
                )

                Spacer(
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .fillMaxWidth()
                        .height(36.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(PieceTheme.colors.light2),
                )

                Spacer(
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .size(width = 180.dp, height = 24.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(PieceTheme.colors.light2),
                )

                Spacer(modifier = Modifier.weight(1f))

                Spacer(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(PieceTheme.colors.light2),
                )
            }
        }
    }
}


@Preview
@Composable
private fun PreviewMatchingLoadingScreen() {
    PieceTheme {
        MatchingLoadingScreen()
    }
}
