package com.puzzle.matching

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.airbnb.mvrx.compose.mavericksViewModel

@Composable
fun MatchingRoute(
    viewModel: MatchingViewModel = mavericksViewModel(),
) {
    MatchingScreen()
}

@Composable
internal fun MatchingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "MatchingRoute",
            fontSize = 30.sp,
        )
    }
}