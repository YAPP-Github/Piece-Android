package com.puzzle.setting.graph.withdraw

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.designsystem.component.PieceRadioList
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.component.PieceSubBackTopBar
import com.puzzle.designsystem.component.PieceTextInputLong
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.setting.graph.withdraw.contract.WithdrawIntent
import com.puzzle.setting.graph.withdraw.contract.WithdrawState
import kotlinx.coroutines.launch

@Composable
internal fun WithdrawRoute(
    viewModel: WithdrawViewModel = mavericksViewModel(),
) {
    val state by viewModel.collectAsState()

    WithdrawScreen(
        state = state,
        onSameWithdrawReasonClick = {
            viewModel.onIntent(WithdrawIntent.OnSameWithdrawReasonClick)
        },
        onWithdrawReasonsClick = {
            viewModel.onIntent(WithdrawIntent.OnWithdrawReasonsClick(it))
        },
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
private fun WithdrawScreen(
    state: WithdrawState,
    onSameWithdrawReasonClick: () -> Unit,
    onWithdrawReasonsClick: (WithdrawState.WithdrawReason) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    var textInput by remember { mutableStateOf("") }
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val isKeyboardOpen = WindowInsets.isImeVisible

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PieceTheme.colors.white)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
            }
            .padding(horizontal = 20.dp)
            .imePadding(),
    ) {
        PieceSubBackTopBar(
            title = "탈퇴하기",
            onBackClick = {},
        )

        if (isKeyboardOpen) {
            PieceRadioList(
                selected = true,
                label = WithdrawState.WithdrawReason.Other.label,
                onSelectedChange = {
                    if (state.selectedReason == WithdrawState.WithdrawReason.Other) {
                        onSameWithdrawReasonClick()
                    }
                },
                modifier = Modifier.padding(top = 6.dp),
            )
        } else {
            Text(
                text = "지금 탈퇴화면\n새로운 인연을 만날 수 없어요",
                style = PieceTheme.typography.headingLSB,
                color = PieceTheme.colors.black,
                modifier = Modifier.padding(top = 20.dp),
            )

            Text(
                text = "탈퇴하는 이유를 알려주세요.\n" +
                        "반영하여 더 나은 사용자 경험을 제공하겠습니다.",
                style = PieceTheme.typography.bodySM,
                color = PieceTheme.colors.dark3,
                modifier = Modifier.padding(top = 12.dp, bottom = 40.dp),
            )

            WithdrawState.WithdrawReason.entries.forEach { reason ->
                PieceRadioList(
                    selected = (reason == state.selectedReason),
                    label = reason.label,
                    onSelectedChange = {
                        onWithdrawReasonsClick(reason)
                        if (reason != WithdrawState.WithdrawReason.Other) {
                            textInput = ""
                        }
                    },
                )
            }
        }

        if (state.selectedReason == WithdrawState.WithdrawReason.Other) {
            PieceTextInputLong(
                value = textInput,
                onValueChange = { input -> textInput = input },
                hint = "자유롭게 작성해 주세요",
                limit = 100,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .onFocusEvent { focusState ->
                        if (focusState.isFocused) {
                            coroutineScope.launch {
                                bringIntoViewRequester.bringIntoView()
                            }
                        }
                    },
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        PieceSolidButton(
            label = "다음",
            onClick = {},
            enabled = state.selectedReason != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
        )
    }
}

@Preview
@Composable
private fun PreviewSettingScreen() {
    PieceTheme {
        WithdrawScreen(
            state = WithdrawState(
                isLoading = false,
                selectedReason = null,
            ),
            onSameWithdrawReasonClick = {},
            onWithdrawReasonsClick = {},
        )
    }
}