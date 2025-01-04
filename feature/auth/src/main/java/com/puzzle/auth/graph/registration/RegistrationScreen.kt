package com.puzzle.auth.graph.registration

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.auth.graph.registration.contract.RegistrationState

@Composable
internal fun RegistrationRoute(
    viewModel: RegistrationViewModel = mavericksViewModel()
) {
    val state by viewModel.collectAsState()

    RegistrationScreen(
        state = state,
    )
}

@Composable
private fun RegistrationScreen(
    state: RegistrationState,
) {
    Column() {

    }
}