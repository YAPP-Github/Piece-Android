package com.puzzle.matching.graph.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.common.ui.repeatOnStarted
import com.puzzle.designsystem.R
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

@Composable
internal fun MatchingTopBar() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 14.dp)
            .fillMaxWidth()
            .height(60.dp),
    ) {
        Text(
            text = stringResource(R.string.matching_title),
            style = PieceTheme.typography.branding,
            color = PieceTheme.colors.white,
        )

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(R.drawable.ic_alarm),
            contentDescription = "",
            modifier = Modifier.size(32.dp),
        )
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
