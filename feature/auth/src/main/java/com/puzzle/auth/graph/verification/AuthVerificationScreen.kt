package com.puzzle.auth.graph.verification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel

@Composable
internal fun AuthVerificationRoute(
    viewModel: AuthVerficationViewModel = mavericksViewModel()
) {
    val state by viewModel.collectAsState()
}
