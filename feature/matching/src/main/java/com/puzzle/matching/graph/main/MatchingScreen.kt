package com.puzzle.matching.graph.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.common.ui.repeatOnStarted
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.domain.model.user.UserRole
import com.puzzle.matching.graph.main.contract.MatchingIntent
import com.puzzle.matching.graph.main.contract.MatchingState
import com.puzzle.matching.graph.main.page.MatchingPendingScreen
import com.puzzle.matching.graph.main.page.MatchingUserScreen
import com.puzzle.navigation.MatchingGraphDest
import com.puzzle.navigation.NavigationEvent

@Composable
internal fun MatchingRoute(
    viewModel: MatchingViewModel = mavericksViewModel(),
) {
    val state by viewModel.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(viewModel) {
        lifecycleOwner.repeatOnStarted {
            viewModel.sideEffects.collect { sideEffect ->
            }
        }
    }

    MatchingScreen(
        state = state,
        onMatchingDetailClick = {
            viewModel.onIntent(
                MatchingIntent.Navigate(
                    NavigationEvent.NavigateTo(
                        MatchingGraphDest.MatchingDetailRoute
                    )
                )
            )
        },
    )
}

@Composable
internal fun MatchingScreen(
    state: MatchingState,
    onMatchingDetailClick: () -> Unit,
) {
    when (state.userRole) {
        UserRole.PENDING -> MatchingPendingScreen(
            onCheckMyProfileClick = {},
        )

        UserRole.USER -> MatchingUserScreen(
            onMatchingDetailClick = onMatchingDetailClick,
        )

        else -> Unit
    }
}

@Preview
@Composable
private fun PreviewMatchingPendingScreen() {
    PieceTheme {
        MatchingPendingScreen(
            onCheckMyProfileClick = {},
        )
    }
}

@Preview
@Composable
private fun PreviewMatchingUserScreen() {
    PieceTheme {
        MatchingUserScreen(
            onMatchingDetailClick = {},
        )
    }
}
