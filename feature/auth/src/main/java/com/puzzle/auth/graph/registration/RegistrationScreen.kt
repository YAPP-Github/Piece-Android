package com.puzzle.auth.graph.registration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.auth.graph.registration.contract.RegistrationIntent
import com.puzzle.auth.graph.registration.contract.RegistrationState
import com.puzzle.designsystem.component.PieceCheckList
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.component.PieceSubBackTopBar
import com.puzzle.designsystem.foundation.PieceTheme
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
        navigate = { event -> viewModel.onIntent(RegistrationIntent.Navigate(event)) }
    )
}

@Composable
private fun RegistrationScreen(
    state: RegistrationState,
    checkAllTerms: () -> Unit,
    checkTerm: (Int) -> Unit,
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
            modifier = Modifier.height(60.dp),
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
            checked = state.agreeAllTerms,
            label = "약관 전체 동의",
            containerColor = PieceTheme.colors.light3,
            onCheckedChange = checkAllTerms,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
        )

        state.termInfos.forEach { termInfo ->
            PieceCheckList(
                checked = state.termsCheckedInfo.getOrDefault(termInfo.termId, false),
                showArrow = true,
                label = termInfo.title,
                onCheckedChange = { checkTerm(termInfo.termId) },
                onArrowClick = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
        )

        PieceSolidButton(
            label = "다음",
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
        )
    }
}