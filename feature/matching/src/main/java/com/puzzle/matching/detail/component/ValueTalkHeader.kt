package com.puzzle.matching.detail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.puzzle.designsystem.foundation.PieceTheme

@Composable
internal fun ValueTalkHeader(
    nickName: String,
    selfDescription: String,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = selfDescription,
            style = PieceTheme.typography.bodyMR,
            color = PieceTheme.colors.black,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = nickName,
                style = PieceTheme.typography.headingLSB,
                color = PieceTheme.colors.primaryDefault,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(28.dp))

            Image(
                painter = painterResource(id = com.puzzle.designsystem.R.drawable.ic_more),
                contentDescription = "basic info 배경화면",
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onMoreClick() },
            )
        }
    }
}