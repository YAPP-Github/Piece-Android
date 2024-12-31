package com.puzzle.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.puzzle.designsystem.R
import com.puzzle.designsystem.foundation.PieceTheme

@Composable
fun PieceDialog(
    dialogTop: @Composable () -> Unit,
    dialogBottom: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Card(
            colors = cardColors().copy(containerColor = PieceTheme.colors.white),
            shape = RoundedCornerShape(12.dp),
            modifier = modifier.padding(horizontal = 32.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 20.dp),
            ) {
                dialogTop()
                dialogBottom()
            }
        }
    }
}

@Composable
fun PieceDialogDefaultTop(
    title: AnnotatedString,
    subText: String,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 40.dp, bottom = 12.dp),
    ) {
        Text(
            text = title,
            color = PieceTheme.colors.black,
            textAlign = TextAlign.Center,
            style = PieceTheme.typography.headingMSB,
        )

        Text(
            text = subText,
            color = PieceTheme.colors.dark2,
            textAlign = TextAlign.Center,
            style = PieceTheme.typography.bodySM,
        )
    }
}

@Composable
fun PieceDialogDefaultTop(
    title: String,
    subText: String,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 40.dp, bottom = 12.dp),
    ) {
        Text(
            text = title,
            color = PieceTheme.colors.black,
            style = PieceTheme.typography.headingMSB,
            textAlign = TextAlign.Center,
        )

        Text(
            text = subText,
            color = PieceTheme.colors.dark2,
            style = PieceTheme.typography.bodySM,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun PieceDialogIconTop(
    @DrawableRes id: Int,
    title: AnnotatedString,
    subText: String,
    contentDescription: String? = null,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 40.dp, bottom = 12.dp),
    ) {
        Image(
            painter = painterResource(id),
            contentDescription = contentDescription,
            modifier = Modifier.size(40.dp),
        )

        Text(
            text = title,
            color = PieceTheme.colors.black,
            style = PieceTheme.typography.headingMSB,
            textAlign = TextAlign.Center,
        )

        Text(
            text = subText,
            color = PieceTheme.colors.dark2,
            style = PieceTheme.typography.bodySM,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun PieceDialogIconTop(
    @DrawableRes id: Int,
    title: String,
    subText: String,
    contentDescription: String? = null,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 40.dp, bottom = 12.dp),
    ) {
        Image(
            painter = painterResource(id),
            contentDescription = contentDescription,
            modifier = Modifier.size(40.dp),
        )

        Text(
            text = title,
            color = PieceTheme.colors.black,
            style = PieceTheme.typography.headingMSB,
            textAlign = TextAlign.Center,
        )

        Text(
            text = subText,
            color = PieceTheme.colors.dark2,
            style = PieceTheme.typography.bodySM,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun PieceDialogBottom(
    leftButtonText: String,
    rightButtonText: String,
    onLeftButtonClick: () -> Unit,
    onRightButtonClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 20.dp),
    ) {
        PieceOutlinedButton(
            label = leftButtonText,
            onClick = onLeftButtonClick,
            modifier = Modifier.weight(1f),
        )

        PieceSolidButton(
            label = rightButtonText,
            onClick = onRightButtonClick,
            modifier = Modifier.weight(1f),
        )
    }
}

@Preview
@Composable
fun PreviewPieceDialogDefault() {
    PieceTheme {
        PieceDialog(
            dialogTop = {
                PieceDialogDefaultTop(
                    title = AnnotatedString("Default Title"),
                    subText = "This is a default subtitle"
                )
            },
            dialogBottom = {
                PieceDialogBottom(
                    leftButtonText = "Label",
                    rightButtonText = "Label",
                    onLeftButtonClick = {},
                    onRightButtonClick = {}
                )
            },
            onDismissRequest = {}
        )
    }
}

@Preview
@Composable
fun PreviewPieceDialogIcon() {
    PieceTheme {
        PieceDialog(
            dialogTop = {
                PieceDialogIconTop(
                    id = R.drawable.ic_close,
                    title = AnnotatedString("Icon Title"),
                    subText = "This is an icon subtitle",
                    contentDescription = "Icon Description"
                )
            },
            dialogBottom = {
                PieceDialogBottom(
                    leftButtonText = "Label",
                    rightButtonText = "Label",
                    onLeftButtonClick = {},
                    onRightButtonClick = {}
                )
            },
            onDismissRequest = {}
        )
    }
}
