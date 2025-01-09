package com.puzzle.auth.graph.registration.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.foundation.PieceTheme

@Composable
internal fun ColumnScope.SignUpCompletedBody(
    onGenerateProfileClick: () -> Unit,
) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = PieceTheme.colors.primaryDefault)) {
                append("Piece")
            }

            append("에 가입하신 것을\n환영해요!")
        },
        style = PieceTheme.typography.headingLSB,
        color = PieceTheme.colors.black,
        modifier = Modifier.padding(top = 80.dp),
    )

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1.2f),
    )

    Spacer(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .size(240.dp)
            .background(PieceTheme.colors.black),
    )

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1.6f),
    )

    PieceSolidButton(
        label = stringResource(R.string.generate_profile),
        onClick = onGenerateProfileClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 10.dp),
    )
}
