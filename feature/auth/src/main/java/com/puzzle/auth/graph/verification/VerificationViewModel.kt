package com.puzzle.auth.graph.verification

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.auth.graph.verification.contract.VerficationState
import com.puzzle.navigation.NavigationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class VerificationViewModel @AssistedInject constructor(
    @Assisted initialState: VerficationState,
    private val navigationHelper: NavigationHelper,
) : MavericksViewModel<VerficationState>(initialState) {
    @AssistedFactory
    interface Factory : AssistedViewModelFactory<VerificationViewModel, VerficationState> {
        override fun create(state: VerficationState): VerificationViewModel
    }

    companion object :
        MavericksViewModelFactory<VerificationViewModel, VerficationState> by hiltMavericksViewModelFactory()
}
