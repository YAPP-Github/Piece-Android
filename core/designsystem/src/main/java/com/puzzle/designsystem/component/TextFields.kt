package com.puzzle.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.puzzle.designsystem.R
import com.puzzle.designsystem.foundation.PieceTheme

@Composable
fun PieceTextInputFields(
    value: String,
    onValueChange: (String) -> Unit,
    @DrawableRes imageId: Int,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    onDone: () -> Unit = {},
    keyboardType: KeyboardType = KeyboardType.Text,
    throttleTime: Long = 2000L,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var lastDoneTime by remember { mutableLongStateOf(0L) }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastDoneTime >= throttleTime) {
                    keyboardController?.hide()
                    onDone()
                    lastDoneTime = currentTime
                }
            }
        ),
        textStyle = PieceTheme.typography.bodyMM,
        cursorBrush = SolidColor(PieceTheme.colors.primaryDefault),
        decorationBox = { innerTextField ->
            Box {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = PieceTheme.typography.bodyMM,
                        color = PieceTheme.colors.dark3,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                }

                innerTextField()

                if (value.isNotEmpty()) {
                    Image(
                        painter = painterResource(imageId),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.CenterEnd)
                            .clickable { onImageClick() },
                    )
                }
            }
        },
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(PieceTheme.colors.light3)
            .padding(horizontal = 16.dp, vertical = 14.dp),
    )
}

@Preview
@Composable
fun PreviewPieceTextInputFields() {
    PieceTheme {
        PieceTextInputFields(
            value = "Label",
            onValueChange = {},
            placeholder = "hint",
            imageId = R.drawable.ic_alarm,
            onImageClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )
    }
}