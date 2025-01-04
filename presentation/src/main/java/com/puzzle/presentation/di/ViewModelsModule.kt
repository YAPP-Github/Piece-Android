package com.puzzle.presentation.di

import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.MavericksViewModelComponent
import com.airbnb.mvrx.hilt.ViewModelKey
import com.puzzle.auth.graph.main.AuthViewModel
import com.puzzle.auth.graph.registration.AuthRegistrationViewModel
import com.puzzle.auth.graph.verification.AuthVerificationViewModel
import com.puzzle.matching.graph.detail.MatchingDetailViewModel
import com.puzzle.matching.graph.main.MatchingViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.multibindings.IntoMap

@Module
@InstallIn(MavericksViewModelComponent::class)
interface ViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(MatchingViewModel::class)
    fun matchingViewModelFactory(factory: MatchingViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(MatchingDetailViewModel::class)
    fun matchingDetailViewModelFactory(factory: MatchingDetailViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    fun authViewModelFactory(factory: AuthViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(AuthRegistrationViewModel::class)
    fun authRegistrationViewModelFactory(factory: AuthRegistrationViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(AuthVerificationViewModel::class)
    fun authVerificationViewModelFactory(factory: AuthVerificationViewModel.Factory): AssistedViewModelFactory<*, *>
}
