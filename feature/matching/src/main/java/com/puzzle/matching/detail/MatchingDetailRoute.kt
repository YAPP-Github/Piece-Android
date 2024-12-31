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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceDialog
import com.puzzle.designsystem.component.PieceDialogBottom
import com.puzzle.designsystem.component.PieceDialogDefaultTop
import com.puzzle.designsystem.component.PieceRoundingButton
import com.puzzle.designsystem.component.PieceSubCloseTopBar
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.matching.detail.content.BasicInfoBody
import com.puzzle.matching.detail.content.ValuePickBody
import com.puzzle.matching.detail.content.ValueTalkBody
import com.puzzle.matching.detail.contract.MatchingDetailIntent
import com.puzzle.matching.detail.contract.MatchingDetailState
import com.puzzle.matching.detail.contract.MatchingDetailState.MatchingDetailPage
import com.puzzle.matching.ui.DialogType
import com.skydoves.cloudy.cloudy

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
    var showDialog by remember { mutableStateOf(false) }
    var dialogType by remember { mutableStateOf(DialogType.ACCEPT_MATCHING) }

    if (showDialog) {
        when (dialogType) {
            DialogType.ACCEPT_MATCHING -> {
                PieceDialog(
                    dialogTop = {
                        PieceDialogDefaultTop(
                            title = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = PieceTheme.colors.primaryDefault)) {
                                    append("수줍은 수달")
                                }
                                append("님과의\n인연을 이어가시겠습니까?")
                            },
                            subText = "서로 매칭을 수락하면, 연락처가 공개됩니다.",
                        )
                    },
                    dialogBottom = {
                        PieceDialogBottom(
                            leftButtonText = "뒤로",
                            rightButtonText = "매칭 수락하기",
                            onLeftButtonClick = { showDialog = false },
                            onRightButtonClick = {},
                        )
                    },
                    onDismissRequest = { showDialog = false },
                )
            }

            DialogType.DECLINE_MATCHING -> {
                PieceDialog(
                    dialogTop = {
                        PieceDialogDefaultTop(
                            title = buildAnnotatedString {
                                append("수줍은 수달님과의\n인연을")
                                withStyle(style = SpanStyle(color = PieceTheme.colors.primaryDefault)) {
                                    append("거절")
                                }
                                append("하시겠습니까?")
                            },
                            subText = "매칭을 거절하면 이후에 되돌릴 수 없으니\n신중히 선택해 주세요.",
                        )
                    },
                    dialogBottom = {
                        PieceDialogBottom(
                            leftButtonText = "뒤로",
                            rightButtonText = "매칭 거절하기",
                            onLeftButtonClick = { showDialog = false },
                            onRightButtonClick = {},
                        )
                    },
                    onDismissRequest = { showDialog = false },
                )
            }
        }
    }

    BackgroundImage(modifier)

    Box(
        modifier = modifier
            .fillMaxSize()
            .then(
                if (state.currentPage != MatchingDetailPage.BasicInfoState) {
                    Modifier.background(PieceTheme.colors.light3)
                } else {
                    Modifier
                }
            )
            .then(
                if (showDialog) {
                    Modifier.cloudy(radius = 8)
                } else {
                    Modifier
                }
            ),
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
            onAcceptClick = {
                dialogType = DialogType.ACCEPT_MATCHING
                showDialog = true
            },
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
private fun BackgroundImage(modifier: Modifier = Modifier) {
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
                BasicInfoBody(
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
                ValueTalkBody(
                    nickName = state.nickName,
                    selfDescription = state.selfDescription,
                    talkCards = state.talkCards,
                    onMoreClick = onMoreClick
                )
            }

            MatchingDetailState.MatchingDetailPage.ValuePickState -> {
                ValuePickBody(
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
