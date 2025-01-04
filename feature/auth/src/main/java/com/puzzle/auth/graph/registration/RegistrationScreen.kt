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
        checkPrivacyAndPolicy = { viewModel.onIntent(RegistrationIntent.CheckPrivacyPolicy) },
        checkTermsOfUse = { viewModel.onIntent(RegistrationIntent.CheckTermsOfUse) },
        navigate = { event -> viewModel.onIntent(RegistrationIntent.Navigate(event)) }
    )
}

@Composable
private fun RegistrationScreen(
    state: RegistrationState,
    checkAllTerms: () -> Unit,
    checkPrivacyAndPolicy: () -> Unit,
    checkTermsOfUse: () -> Unit,
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

        PieceCheckList(
            checked = state.isTermsOfUseChecked,
            showArrow = true,
            label = "[필수] 서비스 이용약관 동의",
            onCheckedChange = checkTermsOfUse,
            onArrowClick = {},
            modifier = Modifier.fillMaxWidth(),
        )

        PieceCheckList(
            checked = state.isPrivacyPolicyChecked,
            showArrow = true,
            label = "[필수] 개인정보처리 방침 동의",
            onCheckedChange = checkPrivacyAndPolicy,
            onArrowClick = {},
            modifier = Modifier.fillMaxWidth(),
        )

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