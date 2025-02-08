package com.puzzle.setting.graph.withdraw.page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceRadio
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.component.PieceTextInputLong
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.setting.graph.withdraw.contract.WithdrawState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun ColumnScope.ReasonPage(
    selectedReason: WithdrawState.WithdrawReason?,
    onReasonsClick: (WithdrawState.WithdrawReason) -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    var textInput by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    var isKeyboardVisible by remember { mutableStateOf(false) }
    AnimatedVisibility(visible = !isKeyboardVisible) {
        Column {
            Text(
                text = stringResource(R.string.reason_page_header),
                style = PieceTheme.typography.headingLSB,
                color = PieceTheme.colors.black,
                modifier = modifier.padding(top = 20.dp),
            )

            Text(
                text = stringResource(R.string.reason_page_second_header),
                style = PieceTheme.typography.bodySM,
                color = PieceTheme.colors.dark3,
                modifier = modifier.padding(top = 12.dp, bottom = 40.dp),
            )

            WithdrawState.WithdrawReason.entries.forEach { reason ->
                if (reason == WithdrawState.WithdrawReason.Other) return@forEach

                PieceRadio(
                    selected = (reason == selectedReason),
                    label = reason.label,
                    onSelectedChange = { onReasonsClick(reason) },
                    modifier = modifier,
                )
            }
        }
    }

    PieceRadio(
        selected = selectedReason == WithdrawState.WithdrawReason.Other,
        label = WithdrawState.WithdrawReason.Other.label,
        onSelectedChange = {
            keyboardController?.hide()
            isKeyboardVisible = false
            onReasonsClick(WithdrawState.WithdrawReason.Other)
        },
        modifier = modifier.padding(top = 6.dp),
    )

    AnimatedVisibility(selectedReason == WithdrawState.WithdrawReason.Other) {
        PieceTextInputLong(
            value = textInput,
            onValueChange = { input -> textInput = input },
            hint = stringResource(R.string.withdraw_other_reaseon_textfield_hint),
            limit = 100,
            modifier = modifier
                .fillMaxWidth()
                .height(160.dp)
                .onFocusEvent { focusState -> isKeyboardVisible = focusState.isFocused },
        )
    }

    Spacer(modifier = modifier.weight(1f))

    val isNextButtonEnabled = selectedReason != null &&
            (selectedReason != WithdrawState.WithdrawReason.Other || textInput.isNotEmpty())
    PieceSolidButton(
        label = "다음",
        enabled = isNextButtonEnabled,
        onClick = {
            coroutineScope.launch {
                keyboardController?.hide()
                delay(300L)
                onNextClick()
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 10.dp),
    )
}

@Preview
@Composable
private fun PreviewConfirmPage() {
    PieceTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PieceTheme.colors.white)
        ) {
            ReasonPage(
                WithdrawState.WithdrawReason.Other,
                {},
                {},
            )
        }
    }
}
