package com.puzzle.auth.graph.signup

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.puzzle.auth.graph.signup.contract.SignUpIntent
import com.puzzle.auth.graph.signup.contract.SignUpSideEffect
import com.puzzle.auth.graph.signup.contract.SignUpSideEffect.Navigate
import com.puzzle.auth.graph.signup.contract.SignUpState
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

class SignUpViewModel @AssistedInject constructor(
    @Assisted initialState: SignUpState,
    private val termsRepository: TermsRepository,
    private val navigationHelper: NavigationHelper,
    private val errorHelper: ErrorHelper,
) : MavericksViewModel<SignUpState>(initialState) {

    private val intents = Channel<SignUpIntent>(BUFFERED)
    private val sideEffects = Channel<SignUpSideEffect>(BUFFERED)

    init {
        fetchTerms()

        intents.receiveAsFlow()
            .onEach(::processIntent)
            .launchIn(viewModelScope)

        sideEffects.receiveAsFlow()
            .onEach(::handleSideEffect)
            .launchIn(viewModelScope)
    }

    internal fun onIntent(intent: SignUpIntent) = viewModelScope.launch {
        intents.send(intent)
    }

    internal fun onSideEffect(sideEffect: SignUpSideEffect) = viewModelScope.launch {
        sideEffects.send(sideEffect)
    }

    private fun processIntent(intent: SignUpIntent) {
        when (intent) {
            is SignUpIntent.CheckAllTerms -> checkAllTerms()
            is SignUpIntent.CheckTerm -> checkTerm(intent.termId)
            is SignUpIntent.AgreeTerm -> agreeTerm(intent.termId)
            is SignUpIntent.OnTermDetailClick -> onTermDetailClick()
            is SignUpIntent.OnBackClick -> onBackClick()
            is SignUpIntent.OnNextClick -> onNextClick()
        }
    }

    private fun handleSideEffect(sideEffect: SignUpSideEffect) {
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
        val updatedTermsCheckedInfo = termsCheckedInfo.toMutableMap()
            .apply { this[termId] = !(this[termId] ?: false) }
            .toMap()

        copy(termsCheckedInfo = updatedTermsCheckedInfo)
    }

    private fun agreeTerm(termId: Int) = setState {
        val updatedTermsCheckedInfo = termsCheckedInfo.toMutableMap()
            .apply { this[termId] = true }
            .toMap()

        copy(
            termsCheckedInfo = updatedTermsCheckedInfo,
            signUpPage = SignUpState.SignUpPage.TermPage,
        )
    }

    private fun checkAllTerms() = setState {
        val updatedTermsCheckedInfo = if (allTermsAgreed) {
            emptyMap()
        } else {
            terms.associate { termInfo -> termInfo.id to true }
        }

        copy(termsCheckedInfo = updatedTermsCheckedInfo)
    }

    private fun onTermDetailClick() = setState {
        copy(signUpPage = SignUpState.SignUpPage.TermDetailPage)
    }

    private fun onBackClick() = setState {
        copy(signUpPage = SignUpState.SignUpPage.getPreviousPage(signUpPage))
    }

    private fun onNextClick() = setState {
        copy(signUpPage = SignUpState.SignUpPage.getNextPage(signUpPage))
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<SignUpViewModel, SignUpState> {
        override fun create(state: SignUpState): SignUpViewModel
    }

    companion object :
        MavericksViewModelFactory<SignUpViewModel, SignUpState> by hiltMavericksViewModelFactory()
}