package com.puzzle.matching.graph.detail.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.puzzle.designsystem.R
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.matching.graph.detail.common.component.BasicInfoHeader

@Composable
internal fun BasicInfoPage(
    nickName: String,
    selfDescription: String,
    birthYear: String,
    age: String,
    height: String,
    religion: String,
    activityRegion: String,
    occupation: String,
    smokeStatue: String,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        BasicInfoName(
            nickName = nickName,
            selfDescription = selfDescription,
            onMoreClick = onMoreClick,
            modifier = Modifier
                .padding(vertical = 20.dp)
                .weight(1f),
        )

        BasicInfoCard(
            age = age,
            birthYear = birthYear,
            height = height,
            religion = religion,
            activityRegion = activityRegion,
            occupation = occupation,
            smokeStatue = smokeStatue,
        )
    }
}

@Composable
private fun BasicInfoName(
    nickName: String,
    selfDescription: String,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.basicinfo_main_label),
            style = PieceTheme.typography.bodyMM,
            color = PieceTheme.colors.primaryDefault,
        )

        Spacer(modifier = Modifier.weight(1f))

        BasicInfoHeader(
            nickName = nickName,
            selfDescription = selfDescription,
            onMoreClick = onMoreClick,
            modifier = Modifier
                .padding(
                    vertical = 20.dp,
                    horizontal = 20.dp
                ),
        )
    }
}

@Composable
private fun BasicInfoCard(
    age: String,
    birthYear: String,
    height: String,
    religion: String,
    activityRegion: String,
    occupation: String,
    smokeStatue: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            InfoItem(
                title = stringResource(R.string.basicinfocard_age),
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(R.string.basicinfocard_age_particle),
                            style = PieceTheme.typography.bodySM,
                            color = PieceTheme.colors.black,
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = age,
                            style = PieceTheme.typography.headingSSB,
                            color = PieceTheme.colors.black,
                        )

                        Text(
                            text = stringResource(R.string.basicinfocard_age_classifier),
                            style = PieceTheme.typography.bodySM,
                            color = PieceTheme.colors.black,
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = birthYear + stringResource(R.string.basicinfocard_age_suffix),
                            style = PieceTheme.typography.bodySM,
                            color = PieceTheme.colors.dark2,
                        )
                    }
                },
                modifier = modifier.size(
                    width = 144.dp,
                    height = 80.dp,
                ),
            )
            InfoItem(
                title = stringResource(R.string.basicinfocard_height),
                content = height,
                modifier = modifier.weight(1f),
            )
            InfoItem(
                title = stringResource(R.string.basicinfocard_religion),
                content = religion,
                modifier = modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(5.5.dp),
        ) {
            InfoItem(
                title = stringResource(R.string.basicinfocard_activityRegion),
                content = activityRegion,
                modifier = modifier.size(
                    width = 144.dp,
                    height = 80.dp,
                ),
            )

            InfoItem(
                title = stringResource(R.string.basicinfocard_occupation),
                content = occupation,
                modifier = modifier.weight(1f),
            )

            InfoItem(
                title = stringResource(R.string.basicinfocard_smokeStatue),
                content = smokeStatue,
                modifier = modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun InfoItem(
    title: String,
    modifier: Modifier = Modifier,
    content: String? = null,
    text: @Composable () -> Unit? = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(PieceTheme.colors.white)
            .padding(vertical = 16.dp, horizontal = 12.dp),
    ) {
        Text(
            text = title,
            style = PieceTheme.typography.bodySM,
            color = PieceTheme.colors.dark2,
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (content != null) {
            Text(
                text = title,
                style = PieceTheme.typography.headingSSB,
                color = PieceTheme.colors.black,
            )
        } else {
            text()
        }
    }
}

@Preview
@Composable
private fun ProfileBasicInfoPagePreview() {
    PieceTheme {
        BasicInfoPage(
            nickName = "수줍은 수달",
            selfDescription = "음악과 요리를 좋아하는",
            birthYear = "1994",
            age = "31",
            height = "200",
            religion = "도교",
            activityRegion = "서울특별시",
            occupation = "개발자",
            smokeStatue = "비흡연",
            onMoreClick = { },
        )
    }
}
