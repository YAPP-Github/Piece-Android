package com.puzzle.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.puzzle.designsystem.R
import com.puzzle.designsystem.foundation.PieceTheme

@Composable
fun PieceCheck(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(R.drawable.ic_check),
        contentDescription = "",
        colorFilter = ColorFilter.tint(
            color = if (checked) PieceTheme.colors.primaryDefault
            else PieceTheme.colors.light1,
        ),
        modifier = modifier.clickable {
            onCheckedChange(!checked)
        },
    )
}

@Composable
fun PieceCheckList(
    checked: Boolean,
    label: String,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    showArrow: Boolean = false,
    containerColor: Color = PieceTheme.colors.white,
    onArrowClick: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(containerColor),
    ) {
        PieceCheck(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(start = 14.dp, end = 12.dp),
        )

        Text(
            text = label,
            style = PieceTheme.typography.bodyMR,
            color = PieceTheme.colors.black,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.weight(1f),
        )

        if (showArrow) {
            Image(
                painter = painterResource(R.drawable.ic_arrow_right),
                contentDescription = "",
                modifier = Modifier
                    .padding(end = 14.dp)
                    .size(24.dp)
                    .clickable { onArrowClick() },
            )
        }
    }
}

@Preview
@Composable
fun PreviewPieceCheck() {
    PieceTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            PieceCheck(
                checked = true,
                onCheckedChange = {},
            )

            PieceCheck(
                checked = false,
                onCheckedChange = {},
            )
        }
    }
}

@Preview
@Composable
fun PreviewPieceCheckList() {
    PieceTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .background(PieceTheme.colors.black)
                .padding(20.dp),
        ) {
            PieceCheckList(
                checked = true,
                label = "약관 전체 동의",
                onCheckedChange = {},
                containerColor = PieceTheme.colors.light3,
                modifier = Modifier.fillMaxWidth(),
            )

            PieceCheckList(
                checked = false,
                showArrow = true,
                label = "약관 전체 동의",
                onCheckedChange = {},
                onArrowClick = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
