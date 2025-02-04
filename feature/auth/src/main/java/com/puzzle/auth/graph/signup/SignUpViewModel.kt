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
    internal val navigationHelper: NavigationHelper,
    private val errorHelper: ErrorHelper,
) : MavericksViewModel<SignUpState>(initialState) {
    private val _intents = Channel<SignUpIntent>(BUFFERED)

    private val _sideEffects = Channel<SignUpSideEffect>(BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        fetchTerms()

        _intents.receiveAsFlow()
            .onEach(::processIntent)
            .launchIn(viewModelScope)
    }

    internal fun onIntent(intent: SignUpIntent) = viewModelScope.launch {
        _intents.send(intent)
    }

    private suspend fun processIntent(intent: SignUpIntent) {
        when (intent) {
            is SignUpIntent.CheckAllTerms -> checkAllTerms()
            is SignUpIntent.CheckTerm -> checkTerm(intent.termId)
            is SignUpIntent.AgreeTerm -> agreeTerm(intent.termId)
            is SignUpIntent.OnTermDetailClick -> onTermDetailClick()
            is SignUpIntent.OnBackClick -> onBackClick()
            is SignUpIntent.OnNextClick -> onNextClick()
            is SignUpIntent.Navigate -> _sideEffects.send(Navigate(intent.navigationEvent))
        }
    }

    private fun fetchTerms() = viewModelScope.launch {
        termsRepository.retrieveTerms().onSuccess {
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
        val updatedTermsCheckedInfo = if (areAllTermsAgreed) {
            emptyMap()
        } else {
            terms.associate { termInfo -> termInfo.id to true }
        }

        copy(termsCheckedInfo = updatedTermsCheckedInfo)
    }

    private fun onTermDetailClick() = setState {
        copy(signUpPage = SignUpState.SignUpPage.TermDetailPage)
    }

    private fun onBackClick() = withState { state ->
        SignUpState.SignUpPage.getPreviousPage(state.signUpPage)?.let {
            setState { copy(signUpPage = it) }
        }
    }

    private fun onNextClick() = withState { state ->
        if (state.signUpPage == SignUpState.SignUpPage.TermPage) {
            viewModelScope.launch {
                val agreeTermsIds = state.termsCheckedInfo.filter { it.value }
                    .map { it.key }
                    .toList()

                termsRepository.agreeTerms(agreeTermsIds)
                    .onSuccess {
                        SignUpState.SignUpPage.getNextPage(state.signUpPage)?.let {
                            setState { copy(signUpPage = it) }
                        }
                    }.onFailure {
                        errorHelper.sendError(it)
                    }
            }
        } else {
            SignUpState.SignUpPage.getNextPage(state.signUpPage)?.let {
                setState { copy(signUpPage = it) }
            }
        }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<SignUpViewModel, SignUpState> {
        override fun create(state: SignUpState): SignUpViewModel
    }

    companion object :
        MavericksViewModelFactory<SignUpViewModel, SignUpState> by hiltMavericksViewModelFactory()
}
