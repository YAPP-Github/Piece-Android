package com.puzzle.auth.graph.registration

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.auth.graph.registration.contract.RegistrationIntent
import com.puzzle.auth.graph.registration.contract.RegistrationSideEffect
import com.puzzle.auth.graph.registration.contract.RegistrationState
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
    private val navigationHelper: NavigationHelper,
) : MavericksViewModel<RegistrationState>(initialState) {

    private val intents = Channel<RegistrationIntent>(BUFFERED)

    private val _sideEffect = Channel<RegistrationSideEffect>(BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        intents.receiveAsFlow()
            .onEach(::processIntent)
            .launchIn(viewModelScope)
    }

    internal fun onIntent(intent: RegistrationIntent) = viewModelScope.launch {
        intents.send(intent)
    }

    private fun processIntent(intent: RegistrationIntent) {
        when (intent) {
            is RegistrationIntent.Navigate -> navigationHelper.navigate(intent.navigationEvent)
            is RegistrationIntent.CheckTerm -> checkTerm(intent.termId)
            is RegistrationIntent.CheckAllTerms -> checkAllTerms()
        }
    }

    private fun checkTerm(termId: Int) = setState {
        val updatedTermsCheckedInfo = termsCheckedInfo.toMutableMap().apply {
            this[termId] = !(this[termId] ?: false)
        }

        copy(termsCheckedInfo = updatedTermsCheckedInfo)
    }

    private fun checkAllTerms() = setState {
        if (agreeAllTerms) {
            copy(termsCheckedInfo = mutableMapOf())
        } else {
            val updatedTermsCheckedInfo = termsCheckedInfo.toMutableMap()

            termInfos.forEach { termInfo ->
                updatedTermsCheckedInfo[termInfo.termId] = true
            }

            copy(termsCheckedInfo = updatedTermsCheckedInfo)
        }
    }

    private fun handleSideEffect(sideEffect: RegistrationSideEffect) {
        when (sideEffect) {
            else -> Unit
        }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<RegistrationViewModel, RegistrationState> {
        override fun create(state: RegistrationState): RegistrationViewModel
    }

    companion object :
        MavericksViewModelFactory<RegistrationViewModel, RegistrationState> by hiltMavericksViewModelFactory()
}