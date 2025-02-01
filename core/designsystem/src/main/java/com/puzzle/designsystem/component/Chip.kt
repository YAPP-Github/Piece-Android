package com.puzzle.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults.filterChipColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
) {
    FilterChip(
        label = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 20.dp),
            ) {
                Text(
                    text = label,
                    style = if (selected) PieceTheme.typography.bodyMSB
                    else PieceTheme.typography.bodyMM,
                    textAlign = TextAlign.Center,
                )
            }
        },
        enabled = enabled,
        selected = selected,
        colors = filterChipColors(
            containerColor = PieceTheme.colors.light3,
            labelColor = PieceTheme.colors.dark2,
            selectedContainerColor = PieceTheme.colors.primaryLight,
            selectedLabelColor = PieceTheme.colors.primaryDefault,
            disabledSelectedContainerColor = PieceTheme.colors.primaryLight,
            disabledContainerColor = PieceTheme.colors.light3,
            disabledLabelColor = if (selected) PieceTheme.colors.primaryDefault
            else PieceTheme.colors.dark2,
        ),
        border = if (enabled && selected) pieceChipSelectedBorder() else null,
        onClick = onChipClicked,
        modifier = modifier,
    )
}

@Composable
private fun pieceChipSelectedBorder(): BorderStroke = BorderStroke(
    width = 1.dp,
    color = PieceTheme.colors.primaryDefault
)

@Preview
@Composable
private fun PreviewPieceChip() {
    PieceTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            PieceChip(
                label = "Non_Select",
                selected = false,
                onChipClicked = {},
                modifier = Modifier
                    .width(300.dp)
                    .padding(16.dp),
            )

            PieceChip(
                label = "Select-Highlight",
                selected = true,
                onChipClicked = {},
                modifier = Modifier
                    .width(300.dp)
                    .padding(16.dp),
            )

            PieceChip(
                label = "Non-Selected",
                selected = false,
                onChipClicked = {},
                enabled = false,
                modifier = Modifier
                    .width(300.dp)
                    .padding(16.dp),
            )

            PieceChip(
                label = "Selected",
                selected = true,
                onChipClicked = {},
                enabled = false,
                modifier = Modifier
                    .width(300.dp)
                    .padding(16.dp),
            )
        }
    }
}
