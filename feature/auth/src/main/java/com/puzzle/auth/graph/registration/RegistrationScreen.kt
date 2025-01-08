package com.puzzle.auth.graph.registration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.auth.graph.registration.contract.RegistrationIntent
import com.puzzle.auth.graph.registration.contract.RegistrationSideEffect
import com.puzzle.auth.graph.registration.contract.RegistrationState
import com.puzzle.auth.graph.registration.ui.TermDetailScreen
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceCheckList
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.component.PieceSubBackTopBar
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.domain.model.terms.Term
import com.puzzle.navigation.NavigationEvent

@Composable
internal fun RegistrationRoute(
    viewModel: RegistrationViewModel = mavericksViewModel()
) {
    val state by viewModel.collectAsState()
    var selectedTerm by remember { mutableStateOf<Term?>(null) }

    selectedTerm?.let { term ->
        TermDetailScreen(
            term = term,
            onBackClick = { selectedTerm = null },
            onAgreeClick = {
                viewModel.onIntent(RegistrationIntent.CheckTerm(term.id))
                selectedTerm = null
            }
        )
    } ?: RegistrationScreen(
        state = state,
        checkAllTerms = { viewModel.onIntent(RegistrationIntent.CheckAllTerms) },
        checkTerm = { viewModel.onIntent(RegistrationIntent.CheckTerm(it)) },
        showTermDetail = { selectedTerm = it },
        navigate = { event -> viewModel.onSideEffect(RegistrationSideEffect.Navigate(event)) }
    )
}

@Composable
private fun RegistrationScreen(
    state: RegistrationState,
    checkAllTerms: () -> Unit,
    checkTerm: (Int) -> Unit,
    showTermDetail: (Term) -> Unit,
    navigate: (NavigationEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
    ) {
        PieceSubBackTopBar(
            title = "",
            onBackClick = { navigate(NavigationEvent.NavigateUp) },
        )

        Text(
            text = buildAnnotatedString {
                append("Piece의\n")

                withStyle(style = SpanStyle(color = PieceTheme.colors.primaryDefault)) {
                    append("이용약관")
                }

                append("을 확인해 주세요")
            },
            style = PieceTheme.typography.headingLSB,
            color = PieceTheme.colors.black,
            modifier = Modifier.padding(top = 20.dp),
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        )

        PieceCheckList(
            checked = state.allTermsAgreed,
            label = stringResource(R.string.all_term_agree),
            containerColor = PieceTheme.colors.light3,
            onCheckedChange = checkAllTerms,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
        )

        state.terms.forEach { term ->
            PieceCheckList(
                checked = state.termsCheckedInfo.getOrDefault(term.id, false),
                arrowEnabled = true,
                label = term.title,
                onCheckedChange = { checkTerm(term.id) },
                onArrowClick = { showTermDetail(term) },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
        )

        PieceSolidButton(
            label = stringResource(R.string.next),
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
        )
    }
}
