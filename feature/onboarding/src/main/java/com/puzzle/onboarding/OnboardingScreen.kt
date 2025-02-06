package com.puzzle.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun OnboardingScreen() {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 2 },
    )

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth()) {

        }

        HorizontalPager(
            state = pagerState,
        ) { page ->
            when (page) {
                0 -> {}
                1 -> {}
            }
        }
    }
}
