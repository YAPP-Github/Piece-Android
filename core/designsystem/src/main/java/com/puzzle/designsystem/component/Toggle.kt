package com.puzzle.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.puzzle.designsystem.foundation.PieceTheme

@Composable
fun PieceToggle(
    checked: Boolean,
    onCheckedChange: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier.size(width = 40.dp, height = 32.dp)) {
        Switch(
            checked = checked,
            onCheckedChange = { onCheckedChange() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = PieceTheme.colors.white,
                checkedTrackColor = PieceTheme.colors.primaryDefault,
                checkedBorderColor = PieceTheme.colors.primaryDefault,
                uncheckedThumbColor = PieceTheme.colors.white,
                uncheckedTrackColor = PieceTheme.colors.light1,
                uncheckedBorderColor = PieceTheme.colors.light1,
            ),
            thumbContent = {
                Box(
                    modifier = Modifier
                        .size(SwitchDefaults.IconSize)
                        .clip(CircleShape)
                        .background(PieceTheme.colors.white)
                )
            },
            modifier = Modifier.padding(horizontal = 3.dp, vertical = 6.dp),
        )
    }
}

@Preview
@Composable
fun PreviewPieceToggle() {
    PieceTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(10.dp)
        ) {
            PieceToggle(
                checked = true,
                onCheckedChange = {},
            )

            PieceToggle(
                checked = false,
                onCheckedChange = {},
            )
        }
    }
}
