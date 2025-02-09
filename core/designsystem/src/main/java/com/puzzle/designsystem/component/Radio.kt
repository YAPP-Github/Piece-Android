package com.puzzle.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.puzzle.common.ui.clickable
import com.puzzle.designsystem.R
import com.puzzle.designsystem.foundation.PieceTheme

@Composable
fun PieceRadio(
    selected: Boolean,
    label: String,
    onSelectedChange: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = PieceTheme.colors.white,
) {
    val radioIcon = if (selected) {
        R.drawable.ic_radio_selected
    } else {
        R.drawable.ic_radio_empty
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(52.dp)
            .background(containerColor)
            .clickable { onSelectedChange() },
    ) {
        Image(
            painter = painterResource(radioIcon),
            contentDescription = null,
            modifier = Modifier
                .padding(end = 12.dp)
                .size(20.dp)
                .clickable { onSelectedChange() },
        )

        Text(
            text = label,
            style = PieceTheme.typography.bodyMR,
            color = PieceTheme.colors.black,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.weight(1f),
        )
    }
}

@Preview
@Composable
fun PieceRadioPreview() {
    PieceTheme {
        Column {
            var selectedIndex by remember { mutableIntStateOf(0) }

            // 여러 개의 RadioRow
            listOf("Option A", "Option B", "Option C").forEachIndexed { index, label ->
                PieceRadio(
                    selected = (index == selectedIndex),
                    label = label,
                    onSelectedChange = {
                        selectedIndex = index
                    },
                )
            }
        }
    }
}

