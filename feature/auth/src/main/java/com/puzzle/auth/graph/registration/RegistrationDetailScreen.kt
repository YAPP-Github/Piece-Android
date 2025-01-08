package com.puzzle.auth.graph.registration

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.component.PieceSubBackTopBar
import com.puzzle.designsystem.component.PieceWebView
import com.puzzle.domain.model.terms.Term

@Composable
internal fun RegistrationDetailScreen(
    term: Term,
    onBackClick: () -> Unit,
    onAgreeClick: () -> Unit,
) {
    BackHandler { onBackClick() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
    ) {
        PieceSubBackTopBar(
            title = term.title,
            onBackClick = onBackClick,
        )

        PieceWebView(
            url = term.content,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        )

        PieceSolidButton(
            label = stringResource(R.string.agree),
            onClick = onAgreeClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 10.dp),
        )
    }
}
