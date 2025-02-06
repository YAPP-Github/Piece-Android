package com.puzzle.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.foundation.PieceTheme

@Composable
internal fun OnboardingRoute() {
    OnboardingScreen()
}

@Composable
internal fun OnboardingScreen() {
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 49.dp)
                .height(64.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_onboarding_logo),
                contentDescription = null,
            )

            Spacer(modifier = Modifier.weight(1f))

            AnimatedVisibility(
                visible = pagerState.currentPage == 0,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Text(
                    text = stringResource(R.string.skip),
                    style = PieceTheme.typography.bodyMM.copy(
                        textDecoration = TextDecoration.Underline
                    ),
                    color = PieceTheme.colors.dark3,
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 40.dp)
        ) { page ->
            Column(modifier = Modifier.fillMaxSize()) {
                when (page) {
                    0 -> OnboardingPageContent(
                        imageRes = R.drawable.ic_onboarding_camera,
                        title = "하루 한 번,\n1:1로 만나는 특별한 인연",
                        description = "매일 밤 10시, 새로운 매칭 조각이 도착해요.\n천천히 프로필을 살펴보고, 맞춰볼지 결정해보세요."
                    )

                    1 -> OnboardingPageContent(
                        imageRes = R.drawable.ic_onboarding_camera,
                        title = "안심하고\n소중한 만남을 즐기세요",
                        description = "스크린샷은 제한되어 있어요.\n오직 이 공간에서만, 편안하게 인연을 찾아보세요."
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
            onClick = {},
            modifier = Modifier
                .padding(bottom = 10.dp, top = 12.dp)
                .fillMaxWidth(),
        )
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
