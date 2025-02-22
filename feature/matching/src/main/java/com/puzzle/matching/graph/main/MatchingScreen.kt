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
import com.puzzle.matching.graph.main.contract.MatchingSideEffect
import com.puzzle.matching.graph.main.contract.MatchingState
import com.puzzle.matching.graph.main.page.MatchingLoadingScreen
import com.puzzle.matching.graph.main.page.MatchingPendingScreen
import com.puzzle.matching.graph.main.page.MatchingUserScreen
import com.puzzle.matching.graph.main.page.MatchingWaitingScreen

@Composable
internal fun MatchingRoute(
    viewModel: MatchingViewModel = mavericksViewModel(),
) {
    val state by viewModel.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(viewModel) {
        lifecycleOwner.repeatOnStarted {
            viewModel.sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is MatchingSideEffect.Navigate -> {
                        viewModel.navigationHelper.navigate(sideEffect.navigationEvent)
                    }
                }
            }
        }

        viewModel.initMatchInfo()
    }

    MatchingScreen(
        state = state,
        onButtonClick = { viewModel.onIntent(MatchingIntent.OnButtonClick) },
        onMatchingDetailClick = { viewModel.onIntent(MatchingIntent.OnMatchingDetailClick) },
        onCheckMyProfileClick = { viewModel.onIntent(MatchingIntent.OnCheckMyProfileClick) },
        onEditProfileClick = { viewModel.onIntent(MatchingIntent.OnEditProfileClick) },
    )
}

@Composable
internal fun MatchingScreen(
    state: MatchingState,
    onButtonClick: () -> Unit,
    onMatchingDetailClick: () -> Unit,
    onCheckMyProfileClick: () -> Unit,
    onEditProfileClick: () -> Unit,
) {
    when (state.userRole) {
        UserRole.PENDING -> MatchingPendingScreen(
            isNotificationEnabled = state.isNotificationEnabled,
            isImageRejected = state.isImageRejected,
            isDescriptionRejected = state.isDescriptionRejected,
            onCheckMyProfileClick = onCheckMyProfileClick,
            onEditProfileClick = onEditProfileClick,
        )

        UserRole.USER -> {
            if (state.matchInfo == null) {
                MatchingWaitingScreen(
                    isNotificationEnabled = state.isNotificationEnabled,
                    onCheckMyProfileClick = onCheckMyProfileClick,
                    remainTime = state.formattedRemainWaitingTime,
                )
            } else {
                when (state.matchInfo.matchStatus) {
                    MatchStatus.UNKNOWN -> MatchingLoadingScreen(isNotificationEnabled = state.isNotificationEnabled)
                    MatchStatus.REFUSED -> MatchingWaitingScreen(
                        isNotificationEnabled = state.isNotificationEnabled,
                        onCheckMyProfileClick = onCheckMyProfileClick,
                        remainTime = state.formattedRemainWaitingTime
                    )

                    else -> MatchingUserScreen(
                        isNotificationEnabled = state.isNotificationEnabled,
                        matchInfo = state.matchInfo,
                        remainTime = state.formattedRemainMatchingStartTime,
                        onButtonClick = onButtonClick,
                        onMatchingDetailClick = onMatchingDetailClick,
                    )
                }
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
            isNotificationEnabled = true,
            isImageRejected = false,
            isDescriptionRejected = false,
            onCheckMyProfileClick = {},
            onEditProfileClick = {},
        )
    }
}

@Preview
@Composable
private fun PreviewMatchingWaitingScreen() {
    PieceTheme {
        MatchingWaitingScreen(
            isNotificationEnabled = true,
            onCheckMyProfileClick = {},
            remainTime = " 00:00:00 ",
        )
    }
}

@Preview
@Composable
private fun PreviewMatchingLoadingScreen() {
    PieceTheme {
        MatchingLoadingScreen(isNotificationEnabled = true)
    }
}

@Preview
@Composable
private fun PreviewMatchingUserScreen() {
    PieceTheme {
        MatchingUserScreen(
            isNotificationEnabled = true,
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
                ),
            ),
            onButtonClick = {},
            onMatchingDetailClick = {},
            remainTime = " 00:00:00 ",
        )
    }
}
