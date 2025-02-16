package com.puzzle.matching.graph.preview

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.common.ui.clickable
import com.puzzle.common.ui.repeatOnStarted
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceImageDialog
import com.puzzle.designsystem.component.PieceLoading
import com.puzzle.designsystem.component.PieceRoundingOutlinedButton
import com.puzzle.designsystem.component.PieceSubCloseTopBar
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.domain.model.profile.MyProfileBasic
import com.puzzle.matching.graph.preview.contract.ProfilePreviewIntent
import com.puzzle.matching.graph.preview.contract.ProfilePreviewSideEffect
import com.puzzle.matching.graph.preview.contract.ProfilePreviewState
import com.puzzle.matching.graph.preview.page.BasicInfoPage
import com.puzzle.matching.graph.preview.page.ValuePickPage
import com.puzzle.matching.graph.preview.page.ValueTalkPage
import com.skydoves.cloudy.cloudy

@Composable
internal fun ProfilePreviewRoute(
    viewModel: ProfilePreviewViewModel = mavericksViewModel(),
) {
    val state by viewModel.collectAsState()

    ProfilePreviewScreen(
        state = state,
        onCloseClick = { viewModel.onIntent(ProfilePreviewIntent.OnCloseClick) },
    )
}

@Composable
private fun ProfilePreviewScreen(
    state: ProfilePreviewState,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var currentPage by remember { mutableStateOf(ProfilePreviewState.Page.BasicInfoPage) }
    var showDialog by remember { mutableStateOf(false) }

    BackgroundImage(modifier = modifier)

    if (showDialog) {
        PieceImageDialog(
            imageUri = state.myProfileBasic?.imageUrl,
            buttonLabel = "매칭 수락하기",
            onDismissRequest = { showDialog = false },
            isApproveButtonShow = false,
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .then(
                if (currentPage != ProfilePreviewState.Page.BasicInfoPage) {
                    Modifier.background(PieceTheme.colors.light3)
                } else {
                    Modifier
                }
            )
            .then(
                if(showDialog){
                    Modifier.cloudy(radius = 40)
                } else {
                    Modifier
                }
            )
    ) {
        val topBarHeight = 60.dp
        val bottomBarHeight = 74.dp

        ProfilePreviewContent(
            state = state,
            currentPage = currentPage,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topBarHeight,
                    bottom = bottomBarHeight,
                ),
        )

        PieceSubCloseTopBar(
            title = currentPage.title,
            onCloseClick = onCloseClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(topBarHeight)
                .align(Alignment.TopCenter)
                .then(
                    if (currentPage != ProfilePreviewState.Page.BasicInfoPage) {
                        Modifier.background(PieceTheme.colors.white)
                    } else {
                        Modifier
                    }
                )
                .padding(horizontal = 20.dp),
        )

        ProfilePreviewBottomBar(
            currentPage = currentPage,
            onNextPageClick = {
                currentPage.getNextPage()?.let { page ->
                    currentPage = page
                }
            },
            onPreviousPageClick = {
                currentPage.getPreviousPage()?.let { page ->
                    currentPage = page
                }
            },
            onShowPicturesClick = { showDialog = true },
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
private fun ProfilePreviewContent(
    state: ProfilePreviewState,
    currentPage: ProfilePreviewState.Page,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (state.myProfileBasic != null && state.myValuePicks.isNotEmpty() && state.myValueTalks.isNotEmpty()) {
            AnimatedContent(
                targetState = currentPage,
                transitionSpec = {
                    fadeIn(tween(700)) togetherWith fadeOut(tween(700))
                },
                modifier = Modifier.fillMaxSize(),
            ) {
                when (it) {
                    ProfilePreviewState.Page.BasicInfoPage ->
                        BasicInfoPage(
                            nickName = state.myProfileBasic.nickname,
                            selfDescription = state.myProfileBasic.description,
                            birthYear = state.myProfileBasic.birthdate,
                            age = state.myProfileBasic.age,
                            height = state.myProfileBasic.height,
                            weight = state.myProfileBasic.weight,
                            location = state.myProfileBasic.location,
                            job = state.myProfileBasic.job,
                            smokingStatus = state.myProfileBasic.smokingStatus,
                        )

                    ProfilePreviewState.Page.ValueTalkPage ->
                        ValueTalkPage(
                            nickName = state.myProfileBasic.nickname,
                            selfDescription = state.myProfileBasic.description,
                            talkCards = state.myValueTalks,
                        )

                    ProfilePreviewState.Page.ValuePickPage ->
                        ValuePickPage(
                            nickName = state.myProfileBasic.nickname,
                            selfDescription = state.myProfileBasic.description,
                            pickCards = state.myValuePicks,
                        )
                }
            }
        } else {
            PieceLoading()
        }
    }
}

@Composable
private fun ProfilePreviewBottomBar(
    currentPage: ProfilePreviewState.Page,
    onPreviousPageClick: () -> Unit,
    onNextPageClick: () -> Unit,
    onShowPicturesClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        if (currentPage == ProfilePreviewState.Page.BasicInfoPage) {
            PieceRoundingOutlinedButton(
                label = stringResource(R.string.check_photo),
                onClick = onShowPicturesClick,
            )
        } else {
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

        if (currentPage == ProfilePreviewState.Page.BasicInfoPage) {
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

        if (currentPage == ProfilePreviewState.Page.ValuePickPage) {
            Image(
                painter = painterResource(id = R.drawable.ic_right_disable),
                contentDescription = "다음 페이지 버튼",
                modifier = Modifier
                    .size(52.dp)
                    .clickable { onNextPageClick() },
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

@Composable
private fun BackgroundImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.matchingdetail_bg),
        contentDescription = "basic info 배경화면",
        contentScale = ContentScale.Crop,
        modifier = modifier.fillMaxSize(),
    )
}

@Preview
@Composable
private fun ProfilePreviewScreenPreview() {
    PieceTheme {
        ProfilePreviewScreen(
            state = ProfilePreviewState(
                myProfileBasic = MyProfileBasic(
                    description = "음악과 요리를 좋아하는",
                    nickname = "수줍은 수달",
                    age = 25,
                    height = 254,
                    weight = 72,
                    job = "개발자",
                    location = "서울특별시",
                    smokingStatus = "비흡연",
                    imageUrl = "",
                    birthdate = "20000101",
                    snsActivityLevel = "TODO()",
                    contacts = emptyList(),
                ),
                myValuePicks = emptyList(),
                myValueTalks = emptyList(),
            ),
            onCloseClick = {},
        )
    }
}
