package com.puzzle.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.onboarding.contract.OnboardingIntent
import kotlinx.coroutines.launch

@Composable
internal fun OnboardingRoute(viewModel: OnboardingViewModel = hiltViewModel()) {
    OnboardingScreen(onStartButtonClick = { viewModel.onIntent(OnboardingIntent.OnStartClick) })
}

@Composable
internal fun OnboardingScreen(onStartButtonClick: () -> Unit) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 2 },
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        OnboardingTopBar(
            currentPage = pagerState.currentPage,
            onSkipButtonClick = onStartButtonClick,
            modifier = Modifier.padding(bottom = 49.dp),
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 40.dp)
        ) { page ->
            Column(modifier = Modifier.fillMaxSize()) {
                when (page) {
                    0 -> OnboardingPageContent(
                        imageRes = R.drawable.ic_onboarding_matching,
                        title = stringResource(R.string.one_day_one_matching_title),
                        description = stringResource(R.string.one_day_one_matching_description),
                    )

                    1 -> OnboardingPageContent(
                        imageRes = R.drawable.ic_onboarding_camera,
                        title = stringResource(R.string.camera_block_title),
                        description = stringResource(R.string.camera_block_description),
                    )
                }
            }
        }

        OnboardingIndicator(
            total = 2,
            current = pagerState.currentPage,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 30.dp),
        )

        PieceSolidButton(
            label = when (pagerState.currentPage) {
                1 -> stringResource(R.string.start)
                else -> stringResource(R.string.next)
            },
            onClick = {
                when (pagerState.currentPage) {
                    1 -> onStartButtonClick()
                    else -> scope.launch { pagerState.animateScrollToPage(1) }
                }
            },
            modifier = Modifier
                .padding(bottom = 10.dp, top = 12.dp)
                .fillMaxWidth(),
        )
    }
}

@Composable
private fun OnboardingTopBar(
    currentPage: Int,
    onSkipButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 20.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.ic_onboarding_logo),
            contentDescription = null,
        )

        Spacer(modifier = Modifier.weight(1f))

        AnimatedVisibility(
            visible = currentPage == 0,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Text(
                text = stringResource(R.string.skip),
                style = PieceTheme.typography.bodyMM.copy(
                    textDecoration = TextDecoration.Underline
                ),
                color = PieceTheme.colors.dark3,
                modifier = Modifier.clickable { onSkipButtonClick() }
            )
        }
    }
}

@Composable
private fun OnboardingPageContent(
    imageRes: Int,
    title: String,
    description: String
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 66.dp),
        )

        Text(
            text = title,
            textAlign = TextAlign.Start,
            style = PieceTheme.typography.headingLSB,
            color = PieceTheme.colors.black,
            modifier = Modifier.padding(bottom = 12.dp),
        )

        Text(
            text = description,
            textAlign = TextAlign.Start,
            style = PieceTheme.typography.bodySM,
            color = PieceTheme.colors.dark3,
        )
    }
}

@Composable
private fun OnboardingIndicator(
    total: Int,
    current: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        (0 until total).forEachIndexed { index, _ ->
            if (index == current) {
                Spacer(
                    modifier = Modifier
                        .size(width = 20.dp, height = 8.dp)
                        .clip(CircleShape)
                        .background(PieceTheme.colors.dark2)
                )
            } else {
                Spacer(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(PieceTheme.colors.light1)
                )
            }
        }
    }
}

@Preview
@Composable
private fun OnboardingScreenPreview() {
    PieceTheme {
        Surface(
            color = PieceTheme.colors.white,
            modifier = Modifier.fillMaxSize(),
        ) {
            OnboardingScreen {}
        }
    }
}

@Preview
@Composable
private fun OnboardingTopBarPreview() {
    PieceTheme {
        OnboardingTopBar(
            currentPage = 0,
            onSkipButtonClick = {}
        )
    }
}


@Preview
@Composable
private fun OnboardingIndicatorPreview() {
    PieceTheme {
        OnboardingIndicator(total = 2, current = 0)
    }
}
