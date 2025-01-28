package com.puzzle.profile.graph.valuepick

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.common.ui.repeatOnStarted
import com.puzzle.profile.graph.valuepick.contract.ValuePickSideEffect
import com.puzzle.profile.graph.valuepick.contract.ValuePickState

@Composable
internal fun ValuePickRoute(
    viewModel: ValuePickViewModel = mavericksViewModel()
) {
    val state by viewModel.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel) {
        lifecycleOwner.repeatOnStarted {
            viewModel.sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is ValuePickSideEffect.Navigate ->
                        viewModel.navigationHelper.navigate(sideEffect.navigationEvent)
                }
            }
        }
    }

    ValuePickScreen(
        state = state,
    )
}

@Composable
private fun ValuePickScreen(
    state: ValuePickState,
    modifier: Modifier = Modifier,
) {

}