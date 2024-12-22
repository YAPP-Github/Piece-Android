package com.puzzle.matching

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.matching.contract.MatchingState
import com.puzzle.navigation.MatchingGraphDest
import com.puzzle.navigation.NavigationEvent
import com.puzzle.navigation.NavigationEvent.NavigateTo

@Composable
fun MatchingRoute(
    viewModel: MatchingViewModel = mavericksViewModel(),
) {
    val state by viewModel.collectAsState()

    MatchingScreen(
        state = state,
        navigate = { viewModel.navigationHelper.navigate(it) },
    )
}

@Composable
internal fun MatchingScreen(
    state: MatchingState,
    navigate: (NavigationEvent) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "MatchingRoute",
            fontSize = 30.sp,
            modifier = Modifier.clickable { navigate(NavigateTo(MatchingGraphDest.MatchingDetailRoute)) }
        )
    }
}