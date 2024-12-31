package com.puzzle.matching.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceRoundingButton
import com.puzzle.designsystem.component.PieceSubCloseTopBar
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.matching.detail.contract.MatchingDetailIntent
import com.puzzle.matching.detail.contract.MatchingDetailState
import com.puzzle.matching.detail.contract.MatchingDetailState.MatchingDetailPage
import com.puzzle.matching.detail.content.ProfileBasicInfoBody
import com.puzzle.matching.detail.content.ProfileValuePickBody
import com.puzzle.matching.detail.content.ProfileValueTalkBody

@Composable
internal fun MatchingDetailRoute(
    viewModel: MatchingDetailViewModel = mavericksViewModel()
) {
    val state by viewModel.collectAsState()

    MatchingDetailScreen(
        state = state,
        onCloseClick = { viewModel.onIntent(MatchingDetailIntent.OnMatchingDetailCloseClick) },
        onPreviousPageClick = { viewModel.onIntent(MatchingDetailIntent.OnPreviousPageClick) },
        onNextPageClick = { viewModel.onIntent(MatchingDetailIntent.OnNextPageClick) },
        onMoreClick = { viewModel.onIntent(MatchingDetailIntent.OnMoreClick) },
    )
}

@Composable
private fun MatchingDetailScreen(
    state: MatchingDetailState,
    onCloseClick: () -> Unit,
    onPreviousPageClick: () -> Unit,
    onNextPageClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BackgroundImage(modifier)

    Box(
        modifier = modifier
            .fillMaxSize()
            .let {
                if (state.currentPage != MatchingDetailPage.BasicInfoState) {
                    it.background(PieceTheme.colors.light3)
                } else {
                    it
                }
            },
    ) {
        val topBarHeight = 60.dp
        val bottomBarHeight = 74.dp

        MatchingDetailContent(
            state = state,
            onMoreClick = onMoreClick,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topBarHeight, bottom = bottomBarHeight),
        )

        PieceSubCloseTopBar(
            title = state.currentPage.title,
            onCloseClick = onCloseClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(topBarHeight)
                .align(Alignment.TopCenter)
                .let {
                    if (state.currentPage != MatchingDetailPage.BasicInfoState) {
                        it.background(PieceTheme.colors.white)
                    } else {
                        it
                    }
                }
                .padding(horizontal = 20.dp),
        )

        MatchingDetailBottomBar(
            currentPage = state.currentPage,
            onNextPageClick = onNextPageClick,
            onPreviousPageClick = onPreviousPageClick,
            onShowPicturesClick = {},
            onAcceptClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(bottomBarHeight)
                .padding(top = 12.dp, bottom = 10.dp)
                .padding(horizontal = 20.dp)
                .align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun BackgroundImage(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.matchingdetail_bg),
            contentDescription = "basic info 배경화면",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
private fun MatchingDetailContent(
    state: MatchingDetailState,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (state.currentPage) {
            MatchingDetailState.MatchingDetailPage.BasicInfoState -> {
                ProfileBasicInfoBody(
                    nickName = state.nickName,
                    selfDescription = state.selfDescription,
                    birthYear = state.birthYear,
                    age = state.age,
                    height = state.height,
                    religion = state.religion,
                    activityRegion = state.activityRegion,
                    occupation = state.occupation,
                    smokeStatue = state.smokeStatue,
                    onMoreClick = onMoreClick,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }

            MatchingDetailState.MatchingDetailPage.ValueTalkState -> {
                ProfileValueTalkBody(
                    nickName = state.nickName,
                    selfDescription = state.selfDescription,
                    talkCards = state.talkCards,
                    onMoreClick = onMoreClick
                )
            }

            MatchingDetailState.MatchingDetailPage.ValuePickState -> {
                ProfileValuePickBody(
                    pickCards = state.pickCards
                )
            }
        }
    }
}

@Composable
private fun MatchingDetailBottomBar(
    currentPage: MatchingDetailPage,
    onPreviousPageClick: () -> Unit,
    onNextPageClick: () -> Unit,
    onShowPicturesClick: () -> Unit,
    onAcceptClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        if (currentPage == MatchingDetailPage.ValuePickState) {
            Image(
                painter = painterResource(id = R.drawable.ic_profile_image_temp),
                contentDescription = "이전 페이지 버튼",
                modifier = Modifier
                    .size(52.dp)
                    .clickable {
                        onShowPicturesClick()
                    },
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        if (currentPage == MatchingDetailPage.BasicInfoState) {
            Image(
                painter = painterResource(id = R.drawable.ic_left_disable),
                contentDescription = "이전 페이지 버튼",
                modifier = Modifier
                    .size(52.dp),
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.ic_left_able),
                contentDescription = "이전 페이지 버튼",
                modifier = Modifier
                    .size(52.dp)
                    .clickable {
                        onPreviousPageClick()
                    },
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        if (currentPage == MatchingDetailPage.ValuePickState) {
            PieceRoundingButton(
                label = stringResource(R.string.feature_matching_detail_valuepick_bottom_bar_label),
                onClick = onAcceptClick,
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.ic_right_able),
                contentDescription = "다음 페이지 버튼",
                modifier = Modifier
                    .size(52.dp)
                    .clickable {
                        onNextPageClick()
                    },
            )
        }
    }
}

@Preview
@Composable
private fun MatchingDetailScreenPreview() {
    PieceTheme {
        MatchingDetailScreen(
            MatchingDetailState(),
            {},
            {},
            {},
            {},
        )
    }
}

@Preview
@Composable
private fun BottomNavigationOnBasicInfoStatePreview() {
    PieceTheme {
        MatchingDetailBottomBar(
            currentPage = MatchingDetailPage.BasicInfoState,
            {},
            {},
            {},
            {},
        )
    }
}

@Preview
@Composable
private fun BottomNavigationOnValueTalkStatePreview() {
    PieceTheme {
        MatchingDetailBottomBar(
            currentPage = MatchingDetailPage.ValueTalkState,
            {},
            {},
            {},
            {},
        )
    }
}

@Preview
@Composable
private fun BottomNavigationOnValuePickStatePreview() {
    PieceTheme {
        MatchingDetailBottomBar(
            currentPage = MatchingDetailPage.ValuePickState,
            {},
            {},
            {},
            {},
        )
    }
}
