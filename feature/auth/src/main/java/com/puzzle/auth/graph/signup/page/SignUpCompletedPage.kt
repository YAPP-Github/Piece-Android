package com.puzzle.auth.graph.signup.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.foundation.PieceTheme

@Composable
internal fun ColumnScope.SignUpCompletedPage(
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

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.anim_piece))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
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

@Preview
@Composable
private fun SignUpCompletedPagePreview() {
    PieceTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
        ) {
            SignUpCompletedPage(
                onGenerateProfileClick = {}
            )
        }
    }
}
