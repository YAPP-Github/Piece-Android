package com.puzzle.auth.graph.registration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.auth.graph.registration.contract.RegistrationIntent
import com.puzzle.auth.graph.registration.contract.RegistrationSideEffect
import com.puzzle.auth.graph.registration.contract.RegistrationState
import com.puzzle.auth.graph.registration.ui.AcessRightsBody
import com.puzzle.auth.graph.registration.ui.TermBody
import com.puzzle.auth.graph.registration.ui.TermDetailBody
import com.puzzle.domain.model.terms.Term
import com.puzzle.navigation.NavigationEvent

@Composable
internal fun RegistrationRoute(
    viewModel: RegistrationViewModel = mavericksViewModel()
) {
    val state by viewModel.collectAsState()

    RegistrationScreen(
        state = state,
        checkAllTerms = { viewModel.onIntent(RegistrationIntent.CheckAllTerms) },
        checkTerm = { viewModel.onIntent(RegistrationIntent.CheckTerm(it)) },
        agreeTerm = { viewModel.onIntent(RegistrationIntent.AgreeTerm(it)) },
        onTermDetailClick = { viewModel.onIntent(RegistrationIntent.OnTermDetailClick) },
        onBackClick = { viewModel.onIntent(RegistrationIntent.OnBackClick) },
        onNextClick = { viewModel.onIntent(RegistrationIntent.OnNextClick) },
        navigate = { event -> viewModel.onSideEffect(RegistrationSideEffect.Navigate(event)) }
    )
}

@Composable
private fun RegistrationScreen(
    state: RegistrationState,
    checkAllTerms: () -> Unit,
    checkTerm: (Int) -> Unit,
    agreeTerm: (Int) -> Unit,
    onTermDetailClick: () -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    navigate: (NavigationEvent) -> Unit,
) {
    val (selectedTerm, setSelectedTerm) = remember { mutableStateOf<Term?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
    ) {
        when (state.registrationPage) {
            RegistrationState.RegistrationPage.TermPage -> TermBody(
                terms = state.terms,
                termsCheckedInfo = state.termsCheckedInfo,
                allTermsAgreed = state.allTermsAgreed,
                checkAllTerms = checkAllTerms,
                checkTerm = checkTerm,
                showTermDetail = {
                    setSelectedTerm(it)
                    onTermDetailClick()
                },
                onBackClick = { navigate(NavigationEvent.NavigateUp) },
                onNextClick = onNextClick,
            )

            RegistrationState.RegistrationPage.TermDetailPage -> TermDetailBody(
                term = selectedTerm!!,
                onBackClick = onBackClick,
                onAgreeClick = { agreeTerm(selectedTerm.id) },
            )

            RegistrationState.RegistrationPage.AccessRightsPage -> AcessRightsBody(
                agreeCameraPermission = false,
                onBackClick = onBackClick,
                onNextClick = onNextClick,
            )

            RegistrationState.RegistrationPage.AvoidAcquaintancesPage -> {}
            RegistrationState.RegistrationPage.SignUpCompleted -> {}
        }
    }
}
