package com.puzzle.matching

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun MatchingRoute(
    onNavigateToDetail: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable { onNavigateToDetail() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = "MatchingRoute", fontSize = 30.sp)
    }
}

@Composable
internal fun MatchingScreen() {
}