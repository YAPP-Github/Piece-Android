package com.puzzle.auth.graph.signup.page

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.puzzle.common.ui.clickable
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.component.PieceSubBackTopBar
import com.puzzle.designsystem.foundation.PieceTheme

@Composable
internal fun ColumnScope.AvoidAcquaintancesPage(
    onBackClick: () -> Unit,
    onTryNextClick: () -> Unit,
    onAvoidAcquaintancesClick: () -> Unit,
) {
    BackHandler { onBackClick() }

    PieceSubBackTopBar(
        title = "",
        onBackClick = onBackClick,
    )

    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = PieceTheme.colors.primaryDefault)) {
                append("아는 사람")
            }

            append("에게는\n프로필이 노출되지 않아요")
        },
        style = PieceTheme.typography.headingLSB,
        color = PieceTheme.colors.black,
        modifier = Modifier.padding(top = 20.dp),
    )

    Text(
        text = stringResource(R.string.avoid_acquaintances_description),
        style = PieceTheme.typography.bodySM,
        color = PieceTheme.colors.dark3,
        modifier = Modifier.padding(top = 12.dp),
    )

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .weight(0.7f),
    )

    Image(
        painter = painterResource(R.drawable.ic_avoid_acquaintances),
        contentDescription = null,
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .size(300.dp),
    )

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1.2f),
    )

    Text(
        text = stringResource(R.string.try_next),
        style = PieceTheme.typography.bodyMM.copy(textDecoration = TextDecoration.Underline),
        color = PieceTheme.colors.dark3,
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(top = 12.dp)
            .clickable { onTryNextClick() },
    )

    PieceSolidButton(
        label = stringResource(R.string.avoid_acquaintances),
        onClick = onAvoidAcquaintancesClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 10.dp),
    )
}

@Preview
@Composable
private fun AvoidAcquaintancesPagePreview() {
    PieceTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
        ) {
            AvoidAcquaintancesPage(
                onBackClick = {},
                onTryNextClick = {},
                onAvoidAcquaintancesClick = {}
            )
        }
    }
}
