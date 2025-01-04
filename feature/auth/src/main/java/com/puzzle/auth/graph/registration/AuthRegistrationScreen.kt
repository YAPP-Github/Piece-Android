package com.puzzle.auth.graph.registration

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel

@Composable
internal fun AuthRegistrationScreen(
    viewModel: AuthRegistrationViewModel = mavericksViewModel()
) {
    val state by viewModel.collectAsState()
}
