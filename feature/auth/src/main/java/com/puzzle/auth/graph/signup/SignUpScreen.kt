package com.puzzle.auth.graph.signup

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
import com.puzzle.auth.graph.signup.contract.SignUpIntent
import com.puzzle.auth.graph.signup.contract.SignUpSideEffect
import com.puzzle.auth.graph.signup.contract.SignUpState
import com.puzzle.auth.graph.signup.page.AccessRightsPage
import com.puzzle.auth.graph.signup.page.AvoidAcquaintancesPage
import com.puzzle.auth.graph.signup.page.SignUpCompletedPage
import com.puzzle.auth.graph.signup.page.TermDetailPage
import com.puzzle.auth.graph.signup.page.TermPage
import com.puzzle.domain.model.terms.Term
import com.puzzle.navigation.NavigationEvent

@Composable
internal fun SignUpRoute(
    viewModel: SignUpViewModel = mavericksViewModel()
) {
    val state by viewModel.collectAsState()

    SignUpScreen(
        state = state,
        checkAllTerms = { viewModel.onIntent(SignUpIntent.CheckAllTerms) },
        checkTerm = { viewModel.onIntent(SignUpIntent.CheckTerm(it)) },
        agreeTerm = { viewModel.onIntent(SignUpIntent.AgreeTerm(it)) },
        onTermDetailClick = { viewModel.onIntent(SignUpIntent.OnTermDetailClick) },
        onBackClick = { viewModel.onIntent(SignUpIntent.OnBackClick) },
        onNextClick = { viewModel.onIntent(SignUpIntent.OnNextClick) },
        navigate = { event -> viewModel.onSideEffect(SignUpSideEffect.Navigate(event)) }
    )
}

@Composable
private fun SignUpScreen(
    state: SignUpState,
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
        when (state.signUpPage) {
            SignUpState.SignUpPage.TermPage -> TermPage(
                terms = state.terms,
                termsCheckedInfo = state.termsCheckedInfo,
                allTermsAgreed = state.isAllTermsAgreed,
                checkAllTerms = checkAllTerms,
                checkTerm = checkTerm,
                showTermDetail = {
                    setSelectedTerm(it)
                    onTermDetailClick()
                },
                onBackClick = { navigate(NavigationEvent.NavigateUp) },
                onNextClick = onNextClick,
            )

            SignUpState.SignUpPage.TermDetailPage -> TermDetailPage(
                term = selectedTerm!!,
                onBackClick = onBackClick,
                onAgreeClick = agreeTerm,
            )

            SignUpState.SignUpPage.AccessRightsPage -> AccessRightsPage(
                agreeCameraPermission = true,
                onBackClick = onBackClick,
                onNextClick = onNextClick,
            )

            SignUpState.SignUpPage.AvoidAcquaintancesPage -> AvoidAcquaintancesPage(
                onBackClick = onBackClick,
                onTryNextClick = onNextClick,
                onAvoidAcquaintancesClick = onNextClick,
            )

            SignUpState.SignUpPage.SignUpCompleted -> SignUpCompletedPage(
                onGenerateProfileClick = {},
            )
        }
    }
}
