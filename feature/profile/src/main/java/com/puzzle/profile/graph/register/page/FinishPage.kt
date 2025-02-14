package com.puzzle.profile.graph.register.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.puzzle.designsystem.R
import com.puzzle.designsystem.foundation.PieceTheme

@Composable
internal fun FinishPage(
    onHomeClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.finish_register_profile_header),
            style = PieceTheme.typography.headingLSB,
            color = PieceTheme.colors.black,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(top = 20.dp)
                .padding(horizontal = 20.dp),
        )

        Text(
            text = stringResource(R.string.finish_register_profile_sub_header),
            style = PieceTheme.typography.bodySM,
            color = PieceTheme.colors.dark3,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 16.dp)
                .padding(horizontal = 20.dp),
        )

        Image(
            painter = painterResource(R.drawable.ic_profile_generate),
            contentDescription = "질문",
            colorFilter = ColorFilter.tint(PieceTheme.colors.dark3),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(300.dp)
                .padding(horizontal = 37.dp)
                .padding(top = 92.dp),
        )

        Spacer(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .weight(1f)
        )

        Text(
            text = stringResource(R.string.finish_register_profile_tohome),
            style = PieceTheme.typography.bodyMM.copy(textDecoration = TextDecoration.Underline),
            color = PieceTheme.colors.dark3,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 20.dp)
                .clickable {
                    onHomeClick()
                },
        )
    }
}

@Preview
@Composable
private fun FinishPagePreview() {
    PieceTheme {
        FinishPage(
            onHomeClick = {},
        )
    }
}
