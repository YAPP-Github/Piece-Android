package com.puzzle.matching.graph.preview.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.ui.graphics.Color
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
    age: Int,
    height: Int,
    weight: Int,
    location: String,
    job: String,
    smokingStatus: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        BasicInfoName(
            nickName = nickName,
            selfDescription = selfDescription,
            modifier = Modifier
                .padding(vertical = 20.dp)
                .weight(1f),
        )

        BasicInfoCard(
            age = age,
            birthYear = birthYear,
            height = height,
            weight = weight,
            location = location,
            job = job,
            smokingStatus = smokingStatus,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
    }
}

@Composable
private fun BasicInfoName(
    nickName: String,
    selfDescription: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(horizontal = 20.dp)) {
        Text(
            text = stringResource(R.string.basicinfo_main_label),
            style = PieceTheme.typography.bodyMM,
            color = PieceTheme.colors.primaryDefault,
        )

        Spacer(modifier = Modifier.weight(1f))

        BasicInfoHeader(
            nickName = nickName,
            selfDescription = selfDescription,
            hasMoreButton = false,
            onMoreClick = {},
        )
    }
}

@Composable
private fun BasicInfoCard(
    age: Int,
    birthYear: String,
    height: Int,
    weight: Int,
    location: String,
    job: String,
    smokingStatus: String,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 4.dp)
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
                        text = age.toString(),
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
                        modifier = Modifier.padding(top = 1.dp),
                    )
                }
            },
            modifier = Modifier.size(
                width = 144.dp,
                height = 80.dp,
            ),
        )

        InfoItem(
            title = stringResource(R.string.basicinfocard_height),
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = height.toString(),
                        style = PieceTheme.typography.headingSSB,
                        color = PieceTheme.colors.black,
                    )

                    Text(
                        text = "cm",
                        style = PieceTheme.typography.bodySM,
                        color = PieceTheme.colors.black,
                    )
                }
            },
            modifier = Modifier.weight(1f),
        )

        InfoItem(
            title = "몸무게",
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = weight.toString(),
                        style = PieceTheme.typography.headingSSB,
                        color = PieceTheme.colors.black,
                    )

                    Text(
                        text = "kg",
                        style = PieceTheme.typography.bodySM,
                        color = PieceTheme.colors.black,
                    )
                }
            },
            modifier = Modifier.weight(1f),
        )
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        InfoItem(
            title = stringResource(R.string.basicinfocard_location),
            content = location,
            modifier = Modifier.size(
                width = 144.dp,
                height = 80.dp,
            ),
        )

        InfoItem(
            title = stringResource(R.string.basicinfocard_job),
            content = job,
            modifier = Modifier.weight(1f),
        )

        InfoItem(
            title = stringResource(R.string.basicinfocard_smokingStatus),
            content = smokingStatus,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun InfoItem(
    title: String,
    modifier: Modifier = Modifier,
    content: String? = null,
    backgroundColor: Color = PieceTheme.colors.white,
    text: @Composable ColumnScope.() -> Unit? = {},
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .height(80.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp),
    ) {
        Text(
            text = title,
            style = PieceTheme.typography.bodySM,
            color = PieceTheme.colors.dark2,
        )

        if (content != null) {
            Text(
                text = content,
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
            age = 31,
            height = 200,
            weight = 72,
            location = "서울특별시",
            job = "개발자",
            smokingStatus = "비흡연",
            modifier = Modifier.background(PieceTheme.colors.dark1)
        )
    }
}
