package com.puzzle.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.puzzle.designsystem.R
import com.puzzle.designsystem.foundation.PieceTheme

@Composable
fun PieceSolidButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PieceTheme.colors.primaryDefault,
            contentColor = PieceTheme.colors.white,
            disabledContainerColor = PieceTheme.colors.light1,
            disabledContentColor = PieceTheme.colors.white,
        ),
        modifier = modifier.height(52.dp),
    ) {
        Text(
            text = label,
            style = PieceTheme.typography.bodyMSB,
        )
    }
}

@Composable
fun PieceOutlinedButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(width = 1.dp, color = PieceTheme.colors.primaryDefault),
        colors = ButtonDefaults.buttonColors(
            containerColor = PieceTheme.colors.white,
            contentColor = PieceTheme.colors.primaryDefault,
            disabledContainerColor = PieceTheme.colors.light1,
            disabledContentColor = PieceTheme.colors.primaryDefault,
        ),
        modifier = modifier.height(52.dp),
    ) {
        Text(
            text = label,
            style = PieceTheme.typography.bodyMSB,
        )
    }
}

@Composable
fun PieceSubButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PieceTheme.colors.primaryLight,
            contentColor = PieceTheme.colors.primaryDefault,
            disabledContainerColor = PieceTheme.colors.light1,
            disabledContentColor = PieceTheme.colors.white,
        ),
        modifier = modifier.height(52.dp),
    ) {
        Text(
            text = label,
            style = PieceTheme.typography.bodyMSB,
        )
    }
}

@Composable
fun PieceIconButton(
    label: String,
    @DrawableRes id: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PieceTheme.colors.primaryDefault,
            contentColor = PieceTheme.colors.white,
            disabledContainerColor = PieceTheme.colors.light1,
            disabledContentColor = PieceTheme.colors.white,
        ),
        modifier = modifier.height(52.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
            )

            Text(
                text = label,
                style = PieceTheme.typography.bodyMSB,
            )
        }
    }
}

@Preview
@Composable
fun PreviewPieceSolidButton() {
    PieceTheme {
        PieceSolidButton(
            label = "Label",
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )
    }
}

@Preview
@Composable
fun PreviewPieceOutlinedButton() {
    PieceTheme {
        PieceOutlinedButton(
            label = "Label",
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )
    }
}

@Preview
@Composable
fun PreviewPieceSubButton() {
    PieceTheme {
        PieceSubButton(
            label = "Label",
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )
    }
}

@Preview
@Composable
fun PreviewPieceDisabledButton() {
    PieceTheme {
        PieceSolidButton(
            label = "Label",
            onClick = {},
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )
    }
}

@Preview
@Composable
fun PreviewPieceIconButton() {
    PieceTheme {
        PieceIconButton(
            label = "Label",
            id = R.drawable.ic_alarm,
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )
    }
}
