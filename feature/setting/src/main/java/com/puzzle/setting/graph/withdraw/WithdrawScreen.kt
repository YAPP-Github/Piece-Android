package com.puzzle.setting.graph.withdraw

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.common.ui.addFocusCleaner
import com.puzzle.common.ui.repeatOnStarted
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceSubBackTopBar
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.setting.graph.withdraw.contract.WithdrawIntent
import com.puzzle.setting.graph.withdraw.contract.WithdrawSideEffect
import com.puzzle.setting.graph.withdraw.contract.WithdrawState
import com.puzzle.setting.graph.withdraw.page.ConfirmPage
import com.puzzle.setting.graph.withdraw.page.ReasonPage

@Composable
internal fun WithdrawRoute(
    viewModel: WithdrawViewModel = mavericksViewModel(),
) {
    val state by viewModel.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel) {
        lifecycleOwner.repeatOnStarted {
            viewModel.sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is WithdrawSideEffect.Navigate -> viewModel.navigationHelper
                        .navigate(sideEffect.navigationEvent)
                }
            }
        }
    }

    WithdrawScreen(
        state = state,
        onReasonsClick = {
            viewModel.onIntent(WithdrawIntent.OnReasonsClick(it))
        },
        onWithdrawClick = {
            viewModel.onIntent(WithdrawIntent.OnWithdrawClick)
        },
        onNextClick = {
            viewModel.onIntent(WithdrawIntent.OnNextClick)
        },
        onBackClick = {
            viewModel.onIntent(WithdrawIntent.onBackClick)
        },
    )
}

@Composable
private fun WithdrawScreen(
    state: WithdrawState,
    onReasonsClick: (WithdrawState.WithdrawReason) -> Unit,
    onWithdrawClick: () -> Unit,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    AnimatedContent(
        targetState = state.currentPage,
        transitionSpec = {
            fadeIn(tween(700)) togetherWith fadeOut(tween(700))
        },
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PieceTheme.colors.white)
                .addFocusCleaner(focusManager)
                .imePadding(),
        ) {
            PieceSubBackTopBar(
                title = stringResource(R.string.withdraw_page),
                onBackClick = onBackClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            )

            HorizontalDivider(
                color = PieceTheme.colors.light2,
                thickness = 1.dp,
            )

            when (it) {
                WithdrawState.WithdrawPage.REASON -> ReasonPage(
                    selectedReason = state.selectedReason,
                    onReasonsClick = onReasonsClick,
                    onNextClick = onNextClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                )

                WithdrawState.WithdrawPage.CONFIRM -> ConfirmPage(
                    onWithdrawClick = onWithdrawClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                )
            }
        }
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
            onReasonsClick = {},
            onWithdrawClick = {},
            onNextClick = {},
            onBackClick = {},
        )
    }
}
