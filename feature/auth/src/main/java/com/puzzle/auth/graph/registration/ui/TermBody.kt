package com.puzzle.auth.graph.registration.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceCheckList
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.component.PieceSubBackTopBar
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.domain.model.terms.Term

@Composable
internal fun ColumnScope.TermBody(
    terms: List<Term>,
    termsCheckedInfo: Map<Int, Boolean>,
    allTermsAgreed: Boolean,
    checkAllTerms: () -> Unit,
    checkTerm: (Int) -> Unit,
    showTermDetail: (Term) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    PieceSubBackTopBar(
        title = "",
        onBackClick = onBackClick,
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
        checked = allTermsAgreed,
        label = stringResource(R.string.all_term_agree),
        containerColor = PieceTheme.colors.light3,
        onCheckedChange = checkAllTerms,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
    )

    terms.forEach { term ->
        PieceCheckList(
            checked = termsCheckedInfo.getOrDefault(term.id, false),
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
        onClick = onNextClick,
        enabled = allTermsAgreed,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 10.dp),
    )
}