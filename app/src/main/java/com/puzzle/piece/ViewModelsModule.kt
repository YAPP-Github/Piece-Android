package com.puzzle.piece

import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.MavericksViewModelComponent
import com.airbnb.mvrx.hilt.ViewModelKey
import com.puzzle.matching.MatchingViewModel
import com.puzzle.matching.detail.MatchingDetailViewModel
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
}