package com.puzzle.auth.graph.signup.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.component.PieceSubBackTopBar
import com.puzzle.designsystem.component.PieceWebView
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.domain.model.terms.Term
import java.time.LocalDateTime

@Composable
internal fun ColumnScope.TermDetailBody(
    term: Term,
    onBackClick: () -> Unit,
    onAgreeClick: () -> Unit,
) {
    BackHandler { onBackClick() }

    PieceSubBackTopBar(
        title = term.title,
        onBackClick = onBackClick,
    )

    PieceWebView(
        url = term.content,
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .clipToBounds(),
    )

    PieceSolidButton(
        label = stringResource(R.string.agree),
        onClick = onAgreeClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 10.dp),
    )
}

@Preview
@Composable
fun TermDetailBodyPreview() {
    PieceTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
        ) {
            TermDetailBody(
                term = Term(1, "개인정보 처리방침", "https://naver.com", true, LocalDateTime.now()),
                onBackClick = {},
                onAgreeClick = {}
            )
        }
    }
}
