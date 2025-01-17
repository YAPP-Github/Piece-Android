package com.puzzle.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults.filterChipColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.puzzle.designsystem.foundation.PieceTheme

@Composable
fun PieceChip(
    label: String,
    selected: Boolean,
    onChipClicked: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    borderStroke: BorderStroke? = null,
) {
    FilterChip(
        label = {
            Text(
                text = label,
                style = PieceTheme.typography.bodyMSB,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        enabled = enabled,
        selected = selected,
        colors = filterChipColors(
            containerColor = PieceTheme.colors.light3,
            labelColor = PieceTheme.colors.dark2,
            selectedLabelColor = PieceTheme.colors.primaryDefault,
            selectedContainerColor = PieceTheme.colors.primaryLight,
        ),
        border = borderStroke,
        onClick = onChipClicked,
        modifier = modifier,
    )
}

@Composable
fun pieceChipSelectedBorder(): BorderStroke = BorderStroke(
    width = 1.dp,
    color = PieceTheme.colors.primaryDefault
)

@Preview
@Composable
private fun PreviewPieceChip() {
    PieceTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            PieceChip(
                label = "Select",
                selected = true,
                onChipClicked = {},
                modifier = Modifier
                    .width(300.dp)
                    .padding(16.dp),
            )

            PieceChip(
                label = "Select-Highlight",
                selected = true,
                onChipClicked = {},
                borderStroke = pieceChipSelectedBorder(),
                modifier = Modifier
                    .width(300.dp)
                    .padding(16.dp),
            )

            PieceChip(
                label = "Non-Select",
                selected = false,
                onChipClicked = {},
                modifier = Modifier
                    .width(300.dp)
                    .padding(16.dp),
            )
        }
    }
}
