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
import com.puzzle.domain.model.match.MatchInfo
import com.puzzle.domain.model.match.MatchStatus
import com.puzzle.domain.model.user.UserRole
import com.puzzle.matching.graph.main.contract.MatchingIntent
import com.puzzle.matching.graph.main.contract.MatchingState
import com.puzzle.matching.graph.main.page.MatchingLoadingScreen
import com.puzzle.matching.graph.main.page.MatchingPendingScreen
import com.puzzle.matching.graph.main.page.MatchingUserScreen
import com.puzzle.matching.graph.main.page.MatchingWaitingScreen
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

        UserRole.USER -> {
            if (state.matchInfo?.matchStatus == MatchStatus.WAITING) {
                MatchingWaitingScreen(
                    onCheckMyProfileClick = {},
                )
            } else {
                state.matchInfo?.let {
                    MatchingUserScreen(
                        matchInfo = state.matchInfo,
                        onMatchingDetailClick = onMatchingDetailClick,
                    )
                } ?: MatchingLoadingScreen()
            }
        }

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
private fun PreviewMatchingWaitingScreen() {
    PieceTheme {
        MatchingWaitingScreen(
            onCheckMyProfileClick = {},
        )
    }
}

@Preview
@Composable
private fun PreviewMatchingLoadingScreen() {
    PieceTheme {
        MatchingLoadingScreen()
    }
}

@Preview
@Composable
private fun PreviewMatchingUserScreen() {
    PieceTheme {
        MatchingUserScreen(
            matchInfo = MatchInfo(
                matchId = 1,
                matchStatus = MatchStatus.WAITING,
                description = "안녕하세요. 저는 활발하고 긍정적인 성격의 소유자입니다.",
                nickname = "별빛소녀",
                birthYear = "1995",
                location = "서울특별시",
                job = "마케팅 전문가",
                matchedValueCount = 3,
                matchedValueList = listOf("여행", "음악", "영화")
            ),
            onMatchingDetailClick = {},
        )
    }
}
