package com.puzzle.auth.graph.registration

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.auth.graph.registration.contract.RegistrationIntent
import com.puzzle.auth.graph.registration.contract.RegistrationSideEffect
import com.puzzle.auth.graph.registration.contract.RegistrationSideEffect.Navigate
import com.puzzle.auth.graph.registration.contract.RegistrationState
import com.puzzle.domain.model.error.ErrorHelper
import com.puzzle.domain.repository.TermsRepository
import com.puzzle.navigation.NavigationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RegistrationViewModel @AssistedInject constructor(
    @Assisted initialState: RegistrationState,
    private val termsRepository: TermsRepository,
    private val navigationHelper: NavigationHelper,
    private val errorHelper: ErrorHelper,
) : MavericksViewModel<RegistrationState>(initialState) {

    private val intents = Channel<RegistrationIntent>(BUFFERED)
    private val sideEffects = Channel<RegistrationSideEffect>(BUFFERED)

    init {
        fetchTerms()

        intents.receiveAsFlow()
            .onEach(::processIntent)
            .launchIn(viewModelScope)

        sideEffects.receiveAsFlow()
            .onEach(::handleSideEffect)
            .launchIn(viewModelScope)
    }

    internal fun onIntent(intent: RegistrationIntent) = viewModelScope.launch {
        intents.send(intent)
    }

    internal fun onSideEffect(sideEffect: RegistrationSideEffect) = viewModelScope.launch {
        sideEffects.send(sideEffect)
    }

    private fun processIntent(intent: RegistrationIntent) {
        when (intent) {
            is RegistrationIntent.CheckTerm -> checkTerm(intent.termId)
            is RegistrationIntent.CheckAllTerms -> checkAllTerms()
        }
    }

    private fun handleSideEffect(sideEffect: RegistrationSideEffect) {
        when (sideEffect) {
            is Navigate -> navigationHelper.navigate(sideEffect.navigationEvent)
        }
    }

    private fun fetchTerms() = viewModelScope.launch {
        termsRepository.getTerms().onSuccess {
            setState { copy(terms = it) }
        }.onFailure { errorHelper.sendError(it) }
    }

    private fun checkTerm(termId: Int) = setState {
        val updatedTermsCheckedInfo = termsCheckedInfo.toMutableMap().apply {
            this[termId] = !(this[termId] ?: false)
        }

        copy(termsCheckedInfo = updatedTermsCheckedInfo)
    }

    private fun checkAllTerms() = setState {
        if (allTermsAgreed) {
            copy(termsCheckedInfo = mutableMapOf())
        } else {
            val updatedTermsCheckedInfo = termsCheckedInfo.toMutableMap()

            terms.forEach { termInfo ->
                updatedTermsCheckedInfo[termInfo.id] = true
            }

            copy(termsCheckedInfo = updatedTermsCheckedInfo)
        }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<RegistrationViewModel, RegistrationState> {
        override fun create(state: RegistrationState): RegistrationViewModel
    }

    companion object :
        MavericksViewModelFactory<RegistrationViewModel, RegistrationState> by hiltMavericksViewModelFactory()
}