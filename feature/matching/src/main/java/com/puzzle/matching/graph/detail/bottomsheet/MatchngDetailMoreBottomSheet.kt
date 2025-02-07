package com.puzzle.matching.graph.detail.bottomsheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.puzzle.designsystem.foundation.PieceTheme

@Composable
internal fun MatchingDetailMoreBottomSheet(
    onBlockClicked: () -> Unit,
    onReportClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, start = 20.dp, end = 20.dp),
    ) {
        Text(
            text = "차단하기",
            textAlign = TextAlign.Start,
            style = PieceTheme.typography.bodyMM,
            color = PieceTheme.colors.black,
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp)
                .clickable { onBlockClicked() }
        )

        Text(
            text = "신고하기",
            textAlign = TextAlign.Start,
            style = PieceTheme.typography.bodyMM,
            color = PieceTheme.colors.black,
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp)
                .clickable { onReportClicked() }
        )
    }
}
