package com.puzzle.matching.graph.detail.common.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.puzzle.common.ui.clickable
import com.puzzle.designsystem.R
import com.puzzle.designsystem.foundation.PieceTheme

@Composable
internal fun BasicInfoHeader(
    nickName: String,
    selfDescription: String,
    modifier: Modifier = Modifier,
    hasMoreButton: Boolean = true,
    onMoreClick: () -> Unit = {}
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier) {
        Text(
            text = selfDescription,
            style = PieceTheme.typography.bodyMR,
            color = PieceTheme.colors.black,
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = nickName,
                style = PieceTheme.typography.headingLSB,
                color = PieceTheme.colors.primaryDefault,
                modifier = Modifier.weight(1f)
            )

            if (hasMoreButton) {
                Image(
                    painter = painterResource(id = R.drawable.ic_more),
                    contentDescription = "basic info 배경화면",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { onMoreClick() },
                )
            }
        }
    }
}

@Preview
@Composable
private fun BasicInfoHeaderPreview() {
    PieceTheme {
        BasicInfoHeader(
            nickName = "nickName",
            selfDescription = "selfDescription",
            onMoreClick = {},
            hasMoreButton = false,
            modifier = Modifier.background(PieceTheme.colors.white)
        )
    }
}
