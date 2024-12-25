package com.puzzle.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.puzzle.designsystem.R
import com.puzzle.designsystem.foundation.PieceTheme

@Composable
fun PieceMainTopBar(
    title: String,
    modifier: Modifier = Modifier,
    titleColor: Color = PieceTheme.colors.black,
    rightComponent: @Composable () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = 20.dp),
    ) {
        Text(
            text = title,
            style = PieceTheme.typography.headingSM,
            color = titleColor,
        )

        Spacer(modifier = Modifier.weight(1f))

        rightComponent()
    }
}

@Composable
fun PieceSubTopBar(
    title: String,
    onNavigationClick: () -> Unit,
    modifier: Modifier = Modifier,
    rightComponent: @Composable () -> Unit = {},
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = 20.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.ic_arrow_left),
            contentDescription = "뒤로 가기 버튼",
            modifier = Modifier.clickable { onNavigationClick() }
        )

        Text(
            text = title,
            style = PieceTheme.typography.headingSM,
            color = PieceTheme.colors.black,
        )

        rightComponent()
    }
}

@Preview(showBackground = true, backgroundColor = 0)
@Composable
fun PreviewPieceMainTopBar() {
    PieceTheme {
        PieceMainTopBar(
            title = "Feature Name",
            rightComponent = {
                Image(
                    painter = painterResource(R.drawable.ic_alarm),
                    contentDescription = "오른쪽 버튼",
                    modifier = Modifier.size(32.dp)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0)
@Composable
fun PreviewPieceSubTopBar() {
    PieceTheme {
        PieceSubTopBar(
            title = "Page Name",
            onNavigationClick = { },
            rightComponent = {
                Image(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = "오른쪽 버튼",
                    modifier = Modifier.size(32.dp),
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
        )
    }
}