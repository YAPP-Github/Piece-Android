package com.puzzle.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import com.puzzle.designsystem.R
import com.puzzle.designsystem.foundation.PieceTheme

@Composable
fun PieceDialog(
    dialogTop: @Composable () -> Unit,
    dialogBottom: @Composable () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
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
    @DrawableRes iconId: Int,
    title: String,
    contentDescription: String? = null,
    subText: String? = null,
    descriptionComposable: @Composable (() -> Unit)? = null,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 40.dp, bottom = 8.dp),
    ) {
        Image(
            painter = painterResource(iconId),
            contentDescription = contentDescription,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .size(40.dp),
        )

        Text(
            text = title,
            color = PieceTheme.colors.black,
            style = PieceTheme.typography.headingMSB,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 12.dp),
        )

        subText?.let {
            Text(
                text = subText,
                color = PieceTheme.colors.dark2,
                style = PieceTheme.typography.bodySM,
                textAlign = TextAlign.Center,
            )
        }

        descriptionComposable?.let {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PieceTheme.colors.light3)
                    .padding(vertical = 12.dp),
            ) {
                descriptionComposable()
            }
        }
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

@Composable
fun PieceImageDialog(
    imageUri: Any?,
    buttonLabel: String,
    onDismissRequest: () -> Unit,
    onApproveClick: () -> Unit = {},
    isApproveButtonShow: Boolean = true,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            PieceSubCloseTopBar(
                title = "",
                contentColor = PieceTheme.colors.white,
                onCloseClick = onDismissRequest,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .height(60.dp)
                    .padding(start = 20.dp, end = 20.dp),
            )

            AsyncImage(
                model = imageUri ?: R.drawable.ic_image_default,
                placeholder = painterResource(R.drawable.ic_image_default),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.Center)
                    .clip(CircleShape)
                    .size(180.dp),
            )

            if (isApproveButtonShow) {
                PieceRoundingSolidButton(
                    label = buttonLabel,
                    onClick = onApproveClick,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 10.dp),
                )
            }
        }
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
                    iconId = R.drawable.ic_close,
                    title = "Icon Title",
                    descriptionComposable = {
                        Text(
                            text = AnnotatedString("This is an icon subtitle"),
                            color = PieceTheme.colors.dark3,
                            style = PieceTheme.typography.bodySM,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(PieceTheme.colors.light3)
                        )
                    },
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

@Preview
@Composable
fun PreviewPieceImageDialog() {
    PieceTheme {
        PieceImageDialog(
            imageUri = R.drawable.ic_image_default,
            buttonLabel = "매칭 수락하기",
            onDismissRequest = {},
            onApproveClick = {},
        )
    }
}
