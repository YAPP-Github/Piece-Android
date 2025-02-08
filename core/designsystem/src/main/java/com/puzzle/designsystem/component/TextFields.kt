package com.puzzle.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.puzzle.common.ui.clickable
import com.puzzle.designsystem.R
import com.puzzle.designsystem.foundation.PieceTheme

@Composable
fun PieceTextInputDefault(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    readOnly: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    throttleTime: Long = 2000L,
    onDone: () -> Unit = {},
    onFocusChanged: (Boolean) -> Unit = {},
    rightComponent: @Composable () -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var lastDoneTime by remember { mutableLongStateOf(0L) }
    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        readOnly = readOnly,
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
            Row {
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty() && !isFocused) {
                        Text(
                            text = hint,
                            style = PieceTheme.typography.bodyMM,
                            color = PieceTheme.colors.dark3,
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                    }

                    innerTextField()
                }

                rightComponent()
            }
        },
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (readOnly) PieceTheme.colors.light2
                else PieceTheme.colors.light3
            )
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
                onFocusChanged(focusState.isFocused)
            },
    )
}

@Composable
fun PieceTextInputLong(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    limit: Int? = null,
    readOnly: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    throttleTime: Long = 2000L,
    onDone: () -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var lastDoneTime by remember { mutableLongStateOf(0L) }
    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        value = value,
        onValueChange = { input ->
            limit?.let { if (input.length <= limit) onValueChange(input) } ?: onValueChange(input)
        },
        singleLine = false,
        readOnly = readOnly,
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
            val isCharCountVisible: Boolean = limit != null && !readOnly
            Box {
                Box(modifier = Modifier.padding(bottom = if (isCharCountVisible) 36.dp else 0.dp)) {
                    if (value.isEmpty()) {
                        Text(
                            text = hint,
                            style = PieceTheme.typography.bodyMM,
                            color = PieceTheme.colors.dark3,
                            modifier = Modifier.align(Alignment.TopStart)
                        )
                    }

                    innerTextField()
                }

                if (isCharCountVisible) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = PieceTheme.colors.primaryDefault)) {
                                append(value.length.toString())
                            }
                            append("/${limit}")
                        },
                        style = PieceTheme.typography.bodySM,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .height(20.dp),
                    )
                }
            }
        },
        modifier = modifier
            .onFocusChanged { focusState -> isFocused = focusState.isFocused }
            .heightIn(min = 160.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(PieceTheme.colors.light3)
            .padding(horizontal = 16.dp, vertical = 14.dp),
    )
}

@Composable
fun PieceTextInputAI(
    value: String,
    onValueChange: (String) -> Unit,
    onSaveClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    throttleTime: Long = 2000L,
    readOnly: Boolean = true,
    onDone: () -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var lastDoneTime by remember { mutableLongStateOf(0L) }
    var isFocused by remember { mutableStateOf(false) }
    var isReadOnly: Boolean by remember { mutableStateOf(readOnly) }
    var isLoading: Boolean by remember { mutableStateOf(value.isBlank()) }

    Column(
        modifier = modifier
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
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
            readOnly = isReadOnly,
            decorationBox = { innerTextField ->
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (isLoading) {
                        Text(
                            text = "작성해주신 내용을 AI가 요약하고 있어요",
                            style = PieceTheme.typography.bodyMM,
                            color = PieceTheme.colors.dark3,
                        )
                    } else {
                        innerTextField()
                    }

                    val imageRes = if (isLoading) {
                        R.drawable.ic_textinput_3dots
                    } else {
                        if (isReadOnly) {
                            R.drawable.ic_textinput_pencil
                        } else {
                            R.drawable.ic_textinput_check
                        }
                    }

                    Image(
                        painter = painterResource(imageRes),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                if (isLoading) return@clickable

                                if (isReadOnly) {
                                    isReadOnly = false
                                } else {
                                    isReadOnly = true
                                    onSaveClick(value)
                                }
                            },
                    )
                }
            },
            modifier = Modifier
                .onFocusChanged { focusState -> isFocused = focusState.isFocused }
                .height(52.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(PieceTheme.colors.primaryLight)
                .padding(horizontal = 16.dp, vertical = 14.dp),
        )

        if (!isReadOnly) {
            Row {
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = PieceTheme.colors.primaryDefault)) {
                            append(value.length.toString())
                        }

                        append("/20")
                    },
                    style = PieceTheme.typography.bodySR,
                    color = PieceTheme.colors.dark3,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun PieceTextInputDropDown(
    value: String,
    onDropDownClick: () -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(PieceTheme.colors.light3)
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .clickable { onDropDownClick() },
    ) {
        Text(
            text = value.ifEmpty { hint },
            style = PieceTheme.typography.bodyMM,
            color = if (value.isNotEmpty()) PieceTheme.colors.black
            else PieceTheme.colors.dark2,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )

        Image(
            painter = painterResource(R.drawable.ic_textinput_dropdown),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
        )
    }
}

@Composable
fun PieceTextInputSnsDropDown(
    value: String,
    @DrawableRes image: Int,
    onValueChange: (String) -> Unit,
    onDropDownClick: () -> Unit,
    modifier: Modifier = Modifier,
    isMandatory: Boolean = false,
    onDeleteClick: () -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(52.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .clip(RoundedCornerShape(8.dp))
                .background(PieceTheme.colors.light3)
                .padding(horizontal = 16.dp, vertical = 14.dp)
                .weight(1f),
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(20.dp),
            )

            Image(
                painter = painterResource(R.drawable.ic_textinput_dropdown),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(24.dp)
                    .clickable { onDropDownClick() },
            )

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                textStyle = PieceTheme.typography.bodyMM,
                cursorBrush = SolidColor(PieceTheme.colors.primaryDefault),
                decorationBox = { innerTextField -> innerTextField() },
                modifier = Modifier.weight(1f)
            )
        }

        if (!isMandatory) {
            Image(
                painter = painterResource(R.drawable.ic_delete_circle),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .clickable { onDeleteClick() },
            )
        }
    }
}

@Preview
@Composable
private fun PreviewPieceTextInputDefault() {
    PieceTheme {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            PieceTextInputDefault(
                value = "Label",
                onValueChange = {},
                hint = "hint",
                rightComponent = {
                    Image(
                        painter = painterResource(R.drawable.ic_delete_circle),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )

            PieceTextInputDefault(
                value = "",
                onValueChange = {},
                hint = "hint",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )

        }
    }
}

@Preview
@Composable
private fun PreviewPieceTextInputLong() {
    PieceTheme {
        PieceTextInputLong(
            value = "Label",
            onValueChange = {},
            limit = 6,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )
    }
}

@Preview
@Composable
private fun PreviewPieceTextInputAI() {
    PieceTheme {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            PieceTextInputAI(
                value = "",
                onValueChange = {},
                onSaveClick = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )

            PieceTextInputAI(
                value = "Label",
                onValueChange = { },
                onSaveClick = { },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )

            PieceTextInputAI(
                value = "Label",
                onValueChange = { },
                onSaveClick = { },
                readOnly = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )
        }
    }
}

@Preview
@Composable
private fun PreviewPieceTextInputDropDown() {
    PieceTheme {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            PieceTextInputDropDown(
                value = "",
                hint = "안내 문구",
                onDropDownClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )

            PieceTextInputDropDown(
                value = "Label",
                onDropDownClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )
        }
    }
}

@Preview
@Composable
private fun PreviewPieceSnsDropDown() {
    PieceTheme {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            PieceTextInputSnsDropDown(
                value = "가나",
                image = R.drawable.ic_sns_kakao,
                isMandatory = true,
                onValueChange = {},
                onDropDownClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )

            PieceTextInputSnsDropDown(
                value = "가나다라마바사",
                image = R.drawable.ic_sns_openchatting,
                isMandatory = false,
                onValueChange = {},
                onDropDownClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )
        }
    }
}
