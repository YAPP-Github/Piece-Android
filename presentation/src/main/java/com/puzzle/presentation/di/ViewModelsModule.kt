package com.puzzle.presentation.di

import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.MavericksViewModelComponent
import com.airbnb.mvrx.hilt.ViewModelKey
import com.puzzle.auth.graph.login.LoginViewModel
import com.puzzle.auth.graph.signup.SignUpViewModel
import com.puzzle.auth.graph.verification.VerificationViewModel
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
    @ViewModelKey(LoginViewModel::class)
    fun authViewModelFactory(factory: LoginViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(SignUpViewModel::class)
    fun authRegistrationViewModelFactory(factory: SignUpViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(VerificationViewModel::class)
    fun authVerificationViewModelFactory(factory: VerificationViewModel.Factory): AssistedViewModelFactory<*, *>
}
