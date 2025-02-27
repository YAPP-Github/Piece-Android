package com.puzzle.matching.graph.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.common.ui.blur
import com.puzzle.common.ui.clickable
import com.puzzle.common.ui.repeatOnStarted
import com.puzzle.common.ui.windowInsetsPadding
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceDialog
import com.puzzle.designsystem.component.PieceDialogBottom
import com.puzzle.designsystem.component.PieceDialogDefaultTop
import com.puzzle.designsystem.component.PieceImageDialog
import com.puzzle.designsystem.component.PieceLoading
import com.puzzle.designsystem.component.PieceRoundingSolidButton
import com.puzzle.designsystem.component.PieceSubCloseTopBar
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.designsystem.foundation.StatusBarColor
import com.puzzle.domain.model.profile.OpponentProfile
import com.puzzle.matching.graph.detail.bottomsheet.MatchingDetailMoreBottomSheet
import com.puzzle.matching.graph.detail.common.constant.DialogType
import com.puzzle.matching.graph.detail.contract.MatchingDetailIntent
import com.puzzle.matching.graph.detail.contract.MatchingDetailSideEffect
import com.puzzle.matching.graph.detail.contract.MatchingDetailState
import com.puzzle.matching.graph.detail.contract.MatchingDetailState.MatchingDetailPage
import com.puzzle.matching.graph.detail.page.BasicInfoPage
import com.puzzle.matching.graph.detail.page.ValuePickPage
import com.puzzle.matching.graph.detail.page.ValueTalkPage

@Composable
internal fun MatchingDetailRoute(
    matchId: Int,
    viewModel: MatchingDetailViewModel = mavericksViewModel()
) {
    val state by viewModel.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(viewModel) {
        lifecycleOwner.repeatOnStarted {
            viewModel.sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is MatchingDetailSideEffect.Navigate -> viewModel.navigationHelper
                        .navigate(sideEffect.navigationEvent)
                }
            }
        }
    }

    MatchingDetailScreen(
        state = state,
        onCloseClick = { viewModel.onIntent(MatchingDetailIntent.OnMatchingDetailCloseClick) },
        onPreviousPageClick = { viewModel.onIntent(MatchingDetailIntent.OnPreviousPageClick) },
        onNextPageClick = { viewModel.onIntent(MatchingDetailIntent.OnNextPageClick) },
        onMoreClick = {
            viewModel.onIntent(
                MatchingDetailIntent.OnMoreClick(
                    {
                        MatchingDetailMoreBottomSheet(
                            onReportClicked = {
                                viewModel.onIntent(MatchingDetailIntent.OnReportClick(matchId))
                            },
                            onBlockClicked = {
                                viewModel.onIntent(MatchingDetailIntent.OnBlockClick(matchId))
                            },
                        )
                    }
                )
            )
        },
        onDeclineClick = { viewModel.onIntent(MatchingDetailIntent.OnDeclineClick) },
        onAcceptClick = { viewModel.onIntent(MatchingDetailIntent.OnAcceptClick) },
    )
}

@Composable
private fun MatchingDetailScreen(
    state: MatchingDetailState,
    onCloseClick: () -> Unit,
    onPreviousPageClick: () -> Unit,
    onNextPageClick: () -> Unit,
    onMoreClick: () -> Unit,
    onDeclineClick: () -> Unit,
    onAcceptClick: () -> Unit,
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
                            onRightButtonClick = {
                                showDialog = false
                                onAcceptClick()
                            },
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
                                append("수줍은 수달님과의\n인연을 ")
                                withStyle(style = SpanStyle(color = PieceTheme.colors.error)) {
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
                            onRightButtonClick = {
                                showDialog = false
                                onDeclineClick()
                            },
                        )
                    },
                    onDismissRequest = { showDialog = false },
                )
            }

            DialogType.PROFILE_IMAGE_DETAIL -> {
                PieceImageDialog(
                    imageUri = state.profile?.imageUrl,
                    buttonLabel = "매칭 수락하기",
                    onApproveClick = { dialogType = DialogType.ACCEPT_MATCHING },
                    onDismissRequest = { showDialog = false },
                )
            }
        }
    }

    BackgroundImage()
    SetStatusBarColor(state.currentPage)

    Box(
        modifier = modifier
            .windowInsetsPadding()
            .fillMaxSize()
            .then(
                if (state.currentPage != MatchingDetailPage.BasicInfoPage) {
                    Modifier.background(PieceTheme.colors.light3)
                } else {
                    Modifier
                }
            )
            .blur(isBlur = showDialog),
    ) {
        val topBarHeight = 60.dp
        val bottomBarHeight = 74.dp

        MatchingDetailContent(
            state = state,
            onMoreClick = onMoreClick,
            onDeclineClick = {
                dialogType = DialogType.DECLINE_MATCHING
                showDialog = true
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topBarHeight, bottom = bottomBarHeight),
        )

        PieceSubCloseTopBar(
            title = state.currentPage.title,
            onCloseClick = onCloseClick,
            closeButtonEnabled = !(showDialog && dialogType == DialogType.PROFILE_IMAGE_DETAIL),
            modifier = Modifier
                .fillMaxWidth()
                .height(topBarHeight)
                .align(Alignment.TopCenter)
                .then(
                    if (state.currentPage != MatchingDetailPage.BasicInfoPage) {
                        Modifier.background(PieceTheme.colors.white)
                    } else {
                        Modifier
                    }
                )
                .padding(horizontal = 20.dp),
        )

        if (!showDialog || dialogType != DialogType.PROFILE_IMAGE_DETAIL) {
            MatchingDetailBottomBar(
                currentPage = state.currentPage,
                onNextPageClick = onNextPageClick,
                onPreviousPageClick = onPreviousPageClick,
                onShowPicturesClick = {
                    dialogType = DialogType.PROFILE_IMAGE_DETAIL
                    showDialog = true
                },
                onAcceptClick = {
                    dialogType = DialogType.ACCEPT_MATCHING
                    showDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(bottomBarHeight)
                    .statusBarsPadding()
                    .padding(top = 12.dp, bottom = 10.dp)
                    .padding(horizontal = 20.dp)
                    .align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
private fun BackgroundImage() {
    Image(
        painter = painterResource(id = R.drawable.matchingdetail_bg),
        contentDescription = "basic info 배경화면",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun SetStatusBarColor(page: MatchingDetailPage) {
    val statusBarColor by animateColorAsState(
        targetValue = when (page) {
            MatchingDetailPage.BasicInfoPage -> Color.Transparent
            else -> PieceTheme.colors.white
        },
        animationSpec = tween(700),
        label = "StatusBarColorAnimation"
    )

    StatusBarColor(statusBarColor)
}

@Composable
private fun MatchingDetailContent(
    state: MatchingDetailState,
    onMoreClick: () -> Unit,
    onDeclineClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        state.profile?.let { profile ->
            AnimatedContent(
                targetState = state.currentPage,
                transitionSpec = {
                    fadeIn(tween(700)) togetherWith fadeOut(tween(700))
                },
                modifier = Modifier.fillMaxSize(),
            ) {
                when (it) {
                    MatchingDetailPage.BasicInfoPage ->
                        BasicInfoPage(
                            nickName = profile.nickname,
                            selfDescription = profile.description,
                            birthYear = profile.birthYear,
                            age = profile.age,
                            height = profile.height,
                            weight = profile.weight,
                            location = profile.location,
                            job = profile.job,
                            smokingStatus = profile.smokingStatus,
                            onMoreClick = onMoreClick,
                        )

                    MatchingDetailPage.ValueTalkPage ->
                        ValueTalkPage(
                            nickName = profile.nickname,
                            selfDescription = profile.description,
                            talkCards = profile.valueTalks,
                            onMoreClick = onMoreClick,
                        )

                    MatchingDetailPage.ValuePickPage ->
                        ValuePickPage(
                            nickName = profile.nickname,
                            selfDescription = profile.description,
                            pickCards = profile.valuePicks,
                            onDeclineClick = onDeclineClick,
                        )
                }
            }
        } ?: PieceLoading()
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
        if (currentPage == MatchingDetailPage.ValuePickPage) {
            Image(
                painter = painterResource(id = R.drawable.ic_profile_image),
                contentDescription = "이전 페이지 버튼",
                modifier = Modifier
                    .size(52.dp)
                    .clickable {
                        onShowPicturesClick()
                    },
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        if (currentPage == MatchingDetailPage.BasicInfoPage) {
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

        if (currentPage == MatchingDetailPage.ValuePickPage) {
            PieceRoundingSolidButton(
                label = stringResource(R.string.accept_matching),
                onClick = onAcceptClick,
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.ic_right_able),
                contentDescription = "다음 페이지 버튼",
                modifier = Modifier
                    .size(52.dp)
                    .clickable { onNextPageClick() },
            )
        }
    }
}

@Preview
@Composable
private fun MatchingDetailScreenPreview() {
    PieceTheme {
        MatchingDetailScreen(
            MatchingDetailState(
                profile = OpponentProfile(
                    description = "음악과 요리를 좋아하는",
                    nickname = "수줍은 수달",
                    birthYear = "00",
                    age = 25,
                    height = 254,
                    weight = 72,
                    job = "개발자",
                    location = "서울특별시",
                    smokingStatus = "비흡연",
                    valueTalks = emptyList(),
                    valuePicks = emptyList(),
                    imageUrl = "",
                )
            ),
            {},
            {},
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
            currentPage = MatchingDetailPage.BasicInfoPage,
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
            currentPage = MatchingDetailPage.ValueTalkPage,
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
            currentPage = MatchingDetailPage.ValuePickPage,
            {},
            {},
            {},
            {},
        )
    }
}
