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
import com.puzzle.domain.model.match.MatchStatus.WAITING
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

        viewModel.initMatchInfo()
    }

    MatchingScreen(
        state = state,
        onButtonClick = { viewModel.onIntent(MatchingIntent.OnButtonClick) },
        onMatchingDetailClick = {
            viewModel.onIntent(
                MatchingIntent.Navigate(
                    NavigationEvent.NavigateTo(MatchingGraphDest.MatchingDetailRoute)
                )
            )
        },
    )
}

@Composable
internal fun MatchingScreen(
    state: MatchingState,
    onButtonClick: () -> Unit,
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
                        onButtonClick = onButtonClick,
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
                matchStatus = WAITING,
                description = "음악과 요리를 좋아하는",
                nickname = "수줍은 수달",
                birthYear = "02",
                location = "광주광역시",
                job = "학생",
                matchedValueCount = 7,
                matchedValueList = listOf(
                    "바깥 데이트 스킨십도 가능",
                    "함께 술을 즐기고 싶어요",
                    "커밍아웃은 가까운 친구에게만 했어요",
                    "바깥 데이트 스킨십도 가능",
                    "함께 술을 즐기고 싶어요",
                    "커밍아웃은 가까운 친구에게만 했어요",
                    "바깥 데이트 스킨십도 가능",
                )
            ),
            onButtonClick = {},
            onMatchingDetailClick = {},
        )
    }
}
