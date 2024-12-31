package com.puzzle.matching.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceRoundingButton
import com.puzzle.designsystem.component.PieceSubButton
import com.puzzle.designsystem.component.PieceSubCloseTopBar
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.domain.model.pick.ValuePick
import com.puzzle.domain.model.value.ValueTalk
import com.puzzle.matching.detail.contract.MatchingDetailIntent
import com.puzzle.matching.detail.contract.MatchingDetailState
import com.puzzle.matching.detail.contract.MatchingDetailState.MatchingDetailPage

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
        onRefuseClick = { },
        onAcceptClick = { },
        onShowPicturesClick = { },
    )
}

@Composable
private fun MatchingDetailScreen(
    state: MatchingDetailState,
    onCloseClick: () -> Unit,
    onPreviousPageClick: () -> Unit,
    onNextPageClick: () -> Unit,
    onMoreClick: () -> Unit,
    onRefuseClick: () -> Unit,
    onShowPicturesClick: () -> Unit,
    onAcceptClick: () -> Unit,
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
            onRefuseClick = onRefuseClick,
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
            onShowPicturesClick = onShowPicturesClick,
            onAcceptClick = onAcceptClick,
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
    onRefuseClick: () -> Unit,
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
                    nickName = state.nickName,
                    selfDescription = state.selfDescription,
                    pickCards = state.pickCards,
                    onRefuseClick = onRefuseClick
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

@Composable
private fun ProfileBasicInfoBody(
    nickName: String,
    selfDescription: String,
    birthYear: String,
    age: String,
    height: String,
    religion: String,
    activityRegion: String,
    occupation: String,
    smokeStatue: String,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        BasicInfoName(
            nickName = nickName,
            selfDescription = selfDescription,
            onMoreClick = onMoreClick,
            modifier = Modifier
                .padding(vertical = 20.dp)
                .weight(1f),
        )

        BasicInfoCard(
            age = age,
            birthYear = birthYear,
            height = height,
            religion = religion,
            activityRegion = activityRegion,
            occupation = occupation,
            smokeStatue = smokeStatue,
        )
    }
}

@Composable
private fun BasicInfoName(
    nickName: String,
    selfDescription: String,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.feature_matching_detail_basicinfo_main_label),
            style = PieceTheme.typography.bodyMM,
            color = PieceTheme.colors.primaryDefault,
        )

        Spacer(modifier = Modifier.weight(1f))

        BasicInfoHeader(
            nickName = nickName,
            selfDescription = selfDescription,
            onMoreClick = onMoreClick,
            modifier = Modifier
                .padding(
                    vertical = 20.dp,
                    horizontal = 20.dp
                ),
        )
    }
}

@Composable
private fun BasicInfoCard(
    age: String,
    birthYear: String,
    height: String,
    religion: String,
    activityRegion: String,
    occupation: String,
    smokeStatue: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            InfoItem(
                title = stringResource(R.string.feature_matching_detail_basicinfocard_age),
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(R.string.feature_matching_detail_basicinfocard_age_particle),
                            style = PieceTheme.typography.bodySM,
                            color = PieceTheme.colors.black,
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = age,
                            style = PieceTheme.typography.headingSSB,
                            color = PieceTheme.colors.black,
                        )

                        Text(
                            text = stringResource(R.string.feature_matching_detail_basicinfocard_age_classifier),
                            style = PieceTheme.typography.bodySM,
                            color = PieceTheme.colors.black,
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = birthYear + stringResource(R.string.feature_matching_detail_basicinfocard_age_suffix),
                            style = PieceTheme.typography.bodySM,
                            color = PieceTheme.colors.dark2,
                        )
                    }
                },
                modifier = modifier.size(
                    width = 144.dp,
                    height = 80.dp,
                ),
            )
            InfoItem(
                title = stringResource(R.string.feature_matching_detail_basicinfocard_height),
                content = height,
                modifier = modifier.weight(1f),
            )
            InfoItem(
                title = stringResource(R.string.feature_matching_detail_basicinfocard_religion),
                content = religion,
                modifier = modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(5.5.dp),
        ) {
            InfoItem(
                title = stringResource(R.string.feature_matching_detail_basicinfocard_activityRegion),
                content = activityRegion,
                modifier = modifier.size(
                    width = 144.dp,
                    height = 80.dp,
                ),
            )

            InfoItem(
                title = stringResource(R.string.feature_matching_detail_basicinfocard_occupation),
                content = occupation,
                modifier = modifier.weight(1f),
            )

            InfoItem(
                title = stringResource(R.string.feature_matching_detail_basicinfocard_smokeStatue),
                content = smokeStatue,
                modifier = modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun InfoItem(
    title: String,
    modifier: Modifier = Modifier,
    content: String? = null,
    text: @Composable () -> Unit? = {},
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(PieceTheme.colors.white)
            .padding(vertical = 16.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            style = PieceTheme.typography.bodySM,
            color = PieceTheme.colors.dark2,
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (content != null) {
            Text(
                text = title,
                style = PieceTheme.typography.headingSSB,
                color = PieceTheme.colors.black,
            )
        } else {
            text()
        }
    }
}

@Composable
private fun ProfileValueTalkBody(
    nickName: String,
    selfDescription: String,
    talkCards: List<ValueTalk>,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    // 1) 고정 헤더 높이(105.dp)
    val valueTalkHeaderHeight = 104.dp
    val valueTalkHeaderHeightPx = with(density) { valueTalkHeaderHeight.roundToPx() }

    // 2) 헤더가 얼마나 접혔는지(offset)를 관리해주는 NestedScrollConnection
    val connection = remember(valueTalkHeaderHeightPx) {
        CollapsingHeaderNestedScrollConnection(valueTalkHeaderHeightPx)
    }

    // 3) 헤더와 리스트 간 공간(Spacer)에 사용할 height (DP)
    //    헤더가 접힐수록 headerOffset이 음수가 되면서 spaceHeight가 줄어듦
    val spaceHeight by remember(density) {
        derivedStateOf {
            with(density) {
                // (헤더 높이 + offset)를 DP로 변환
                // offset이 -105px이면 0dp,
                // offset이 0px이면 105dp
                (valueTalkHeaderHeightPx + connection.headerOffset).toDp()
            }
        }
    }

    // 4) Box에 nestedScroll(connection)을 달아, 스크롤 이벤트가
    //    CollapsingHeaderNestedScrollConnection으로 전달되도록 함
    Box(
        modifier = modifier
            .nestedScroll(connection)
    ) {
        // 5) Column: Spacer + LazyColumn을 세로로 배치
        //    헤더가 접힐수록 Spacer의 높이가 줄어들고, 그만큼 리스트가 위로 올라옴
        Column {
            // 5-1) 헤더 높이만큼 Spacer를 줘서 리스트가 '헤더 아래'에서 시작
            //      헤더 offset이 변하면, spaceHeight가 변해 리스트도 따라 위로 올라감
            Spacer(
                Modifier.height(spaceHeight)
            )

            ValueTalkCards(talkCards)
        }

        // 6) 실제 헤더 뷰
        //    offset을 통해 y축 이동 (headerOffset이 음수면 위로 올라가며 사라짐)
        BasicInfoHeader(
            nickName = nickName,
            selfDescription = selfDescription,
            onMoreClick = onMoreClick,
            modifier = Modifier
                .offset { IntOffset(0, connection.headerOffset) }
                .background(PieceTheme.colors.white)
                .height(valueTalkHeaderHeight)
                .padding(
                    vertical = 20.dp,
                    horizontal = 20.dp
                ),
        )

        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(PieceTheme.colors.light2)
                .align(Alignment.TopCenter),
        )
    }
}

@Composable
private fun ValueTalkCards(talkCards: List<ValueTalk>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(PieceTheme.colors.light3)
            .padding(horizontal = 20.dp),
    ) {
        itemsIndexed(talkCards) { idx, item ->
            Spacer(Modifier.height(20.dp))

            ValueTalkCard(
                item = item,
                idx = idx,
            )
        }

        item {
            Spacer(Modifier.height(60.dp))
        }
    }
}

private class CollapsingHeaderNestedScrollConnection(
    val headerHeight: Int
) : NestedScrollConnection {

    // 헤더 offset(픽셀 단위), 0이면 펼침, -headerHeight이면 완전 접힘
    var headerOffset: Int by mutableIntStateOf(0)
        private set

    // 스크롤 이벤트가 오기 전, 먼저 얼마나 소모할지 계산
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        // y축 델타(수직 스크롤 양)
        val delta = available.y.toInt()

        // 새 offset = 기존 offset + 스크롤 델타
        val newOffset = headerOffset + delta
        val previousOffset = headerOffset

        // -headerHeight ~ 0 사이로 제한
        //   -> 최소 -105: 완전히 접힘, 최대 0: 완전히 펼침
        headerOffset = newOffset.coerceIn(-headerHeight, 0)

        // 소비(consumed)된 스크롤 양 = (바뀐 offset - 기존 offset)
        val consumed = headerOffset - previousOffset

        // x축은 소비 안 함(0f), y축은 consumed만큼 소비했다고 반환
        return Offset(0f, consumed.toFloat())
    }
}

@Composable
private fun BasicInfoHeader(
    nickName: String,
    selfDescription: String,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = selfDescription,
            style = PieceTheme.typography.bodyMR,
            color = PieceTheme.colors.black,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = nickName,
                style = PieceTheme.typography.headingLSB,
                color = PieceTheme.colors.primaryDefault,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(28.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_more),
                contentDescription = "basic info 배경화면",
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        onMoreClick()
                    },
            )
        }
    }
}

@Composable
private fun ValueTalkCard(
    item: ValueTalk,
    idx: Int,
    modifier: Modifier = Modifier,
) {
    val icons = remember {
        listOf(
            R.drawable.ic_puzzle1,
            R.drawable.ic_puzzle2,
            R.drawable.ic_puzzle3,
        )
    }

    val idxInRange = idx % icons.size

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(PieceTheme.colors.white)
            .padding(
                top = 20.dp,
                bottom = 32.dp,
                start = 20.dp,
                end = 20.dp,
            )
    ) {
        Text(
            text = item.label,
            style = PieceTheme.typography.bodyMM,
            color = PieceTheme.colors.primaryDefault,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Image(
            painter = painterResource(id = icons[idxInRange]),
            contentDescription = "basic info 배경화면",
            modifier = Modifier
                .size(60.dp),
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = item.title,
            style = PieceTheme.typography.headingMSB,
            color = PieceTheme.colors.black,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = item.content,
            style = PieceTheme.typography.bodySM,
            color = PieceTheme.colors.dark2,
        )
    }
}

@Composable
private fun ProfileValuePickBody(
    nickName: String,
    selfDescription: String,
    pickCards: List<ValuePick>,
    onRefuseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current

    val valuePickHeaderHeight = 104.dp

    val valuePickHeaderHeightPx = with(density) { valuePickHeaderHeight.roundToPx() }

    val connection = remember(valuePickHeaderHeightPx) {
        CollapsingHeaderNestedScrollConnection(valuePickHeaderHeightPx)
    }

    val spaceHeight by remember(density) {
        derivedStateOf {
            with(density) {
                (valuePickHeaderHeightPx + connection.headerOffset).toDp()
            }
        }
    }

    val tabIndex = rememberSaveable { mutableIntStateOf(0) }

    val tabTitles = listOf(
        stringResource(R.string.feature_matching_detail_valuepick_all),
        stringResource(R.string.feature_matching_detail_valuepick_same),
        stringResource(R.string.feature_matching_detail_valuepick_different),
    )

    Box(
        modifier = modifier
            .nestedScroll(connection)
    ) {
        Column {
            Spacer(Modifier.height(spaceHeight))

            Column(
                modifier = modifier.fillMaxSize(),
            ) {
                ValuePickTabRow(
                    tabIndex = tabIndex.intValue,
                    tabTitles = tabTitles,
                    onTabClick = { tabIndex.intValue = it.toInt() },
                )

                ValuePickTabContent(
                    tabIndex = tabIndex.intValue,
                    pickCards = pickCards,
                    onRefuseClick = onRefuseClick,
                )
            }
        }

        BasicInfoHeader(
            nickName = nickName,
            selfDescription = selfDescription,
            onMoreClick = { },
            modifier = Modifier
                .offset { IntOffset(0, connection.headerOffset) }
                .background(PieceTheme.colors.white)
                .height(valuePickHeaderHeight)
                .padding(
                    vertical = 20.dp,
                    horizontal = 20.dp
                ),
        )

        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(PieceTheme.colors.light2)
                .align(Alignment.TopCenter),
        )
    }
}

@Composable
private fun ValuePickTabContent(
    tabIndex: Int,
    pickCards: List<ValuePick>,
    onRefuseClick: () -> Unit,
) {
    when (tabIndex) {
        ALL -> {
            ValuePickCards(
                pickCards = pickCards,
                onRefuseClick = onRefuseClick,
            )
        }

        SAME -> {
            ValuePickCards(
                pickCards = pickCards.filter { it.isSimilarToMe },
                onRefuseClick = onRefuseClick,
            )
        }

        DIFFERENT -> {
            ValuePickCards(
                pickCards = pickCards.filterNot { it.isSimilarToMe },
                onRefuseClick = onRefuseClick,
            )
        }
    }
}

const val ALL = 0
const val SAME = 1
const val DIFFERENT = 2

@Composable
private fun ValuePickCards(
    pickCards: List<ValuePick>,
    onRefuseClick: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(PieceTheme.colors.light3)
            .padding(horizontal = 20.dp),
    ) {
        itemsIndexed(pickCards) { idx, item ->
            Spacer(Modifier.height(20.dp))

            ValuePickCard(
                valuePick = item,
            )
        }

        item {
            Spacer(Modifier.height(60.dp))

            Text(
                text = stringResource(R.string.feature_matching_detail_valuepick_refuse),
                style = PieceTheme.typography.bodyMM.copy(
                    textDecoration = TextDecoration.Underline
                ),
                color = PieceTheme.colors.dark3,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onRefuseClick()
                    },
            )

            Spacer(Modifier.height(60.dp))
        }
    }
}

@Composable
private fun ValuePickTabRow(
    tabIndex: Int,
    onTabClick: (Int) -> Unit,
    tabTitles: List<String>
) {
    TabRow(
        containerColor = PieceTheme.colors.white,
        selectedTabIndex = tabIndex,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        indicator = { tabPositions ->
            if (tabIndex < tabPositions.size) {
                TabRowDefaults.SecondaryIndicator(
                    color = PieceTheme.colors.black,
                    modifier = Modifier.tabIndicatorOffset(tabPositions[tabIndex]),
                )
            }
        },
        divider = {},
    ) {
        tabTitles.forEachIndexed { index, title ->
            Tab(
                selected = (tabIndex == index),
                onClick = { onTabClick(index) },
                text = {
                    Text(
                        text = title,
                        style = PieceTheme.typography.bodyMM,
                    )
                },
                selectedContentColor = PieceTheme.colors.black,
                unselectedContentColor = PieceTheme.colors.dark3,
            )
        }
    }
}

@Composable
private fun ValuePickCard(
    valuePick: ValuePick,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(PieceTheme.colors.white)
            .padding(
                horizontal = 20.dp,
                vertical = 24.dp,
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_question),
                contentDescription = "basic info 배경화면",
                modifier = Modifier
                    .size(20.dp),
            )

            Spacer(modifier = modifier.width(6.dp))

            Text(
                text = valuePick.category,
                style = PieceTheme.typography.bodySSB,
                color = PieceTheme.colors.primaryDefault,
            )

            Spacer(modifier = modifier.weight(1f))

            if (valuePick.isSimilarToMe) {
                Text(
                    text = "나와 같은",
                    style = PieceTheme.typography.captionM,
                    color = PieceTheme.colors.subDefault,
                    modifier = Modifier
                        .clip(RoundedCornerShape(23.dp))
                        .background(PieceTheme.colors.subLight)
                        .padding(vertical = 6.dp, horizontal = 12.dp),
                    textAlign = TextAlign.Center,
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = valuePick.question,
            style = PieceTheme.typography.headingMSB,
            color = PieceTheme.colors.dark1,
        )

        Spacer(modifier = Modifier.height(24.dp))

        PieceSubButton(
            label = valuePick.option1,
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
        )

        Spacer(modifier = Modifier.height(8.dp))

        PieceSubButton(
            label = valuePick.option2,
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
        )
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
            {},
            {},
            {},
        )
    }
}

@Preview
@Composable
private fun ProfileBasicInfoBodyPreview() {
    PieceTheme {
        ProfileBasicInfoBody(
            nickName = "수줍은 수달",
            selfDescription = "음악과 요리를 좋아하는",
            birthYear = "1994",
            age = "31",
            height = "200",
            religion = "도교",
            activityRegion = "서울특별시",
            occupation = "개발자",
            smokeStatue = "비흡연",
            onMoreClick = { },
        )
    }
}

@Preview
@Composable
private fun ProfileValueTalkBodyPreview() {
    PieceTheme {
        ProfileValueTalkBody(
            nickName = "수줍은 수달",
            selfDescription = "음악과 요리를 좋아하는",
            onMoreClick = {},
            talkCards = listOf(
                ValueTalk(
                    label = "꿈과 목표",
                    title = "여행하며 문화 경험, LGBTQ+ 변화를 원해요.",
                    content = "안녕하세요! 저는 삶의 매 순간을 소중히 여기며, 꿈과 목표를 이루기 위해 노력하는 사람입니다. 제 가장 큰 꿈은 여행을 통해 다양한 문화와 사람들을 경험하고, 그 과정에서 얻은 지혜를 나누는 것입니다. 또한, LGBTQ+ 커뮤니티를 위한 긍정적인 변화를 이끌어내고 싶습니다. 내가 이루고자 하는 목표는 나 자신을 발전시키고, 사랑하는 사람들과 함께 행복한 순간들을 만드는 것입니다. 서로의 꿈을 지지하며 함께 성장할 수 있는 관계를 기대합니다!",
                ),
                ValueTalk(
                    label = "관심사와 취향",
                    title = "음악, 요리, 하이킹을 좋아해요.",
                    content = "저는 다양한 취미와 관심사를 가진 사람입니다. 음악을 사랑하여 콘서트에 자주 가고, 특히 인디 음악과 재즈에 매력을 느낍니다. 요리도 좋아해 새로운 레시피에 도전하는 것을 즐깁니다. 여행을 통해 새로운 맛과 문화를 경험하는 것도 큰 기쁨입니다. 또, 자연을 사랑해서 주말마다 하이킹이나 캠핑을 자주 떠납니다. 영화와 책도 좋아해, 좋은 이야기와 감동을 나누는 시간을 소중히 여깁니다. 서로의 취향을 공유하며 즐거운 시간을 보낼 수 있기를 기대합니다!",
                ),
                ValueTalk(
                    label = "연애관",
                    title = "서로 존중하고 신뢰하며, 함께 성장하는 관계를 원해요. ",
                    content = "저는 연애에서 서로의 존중과 신뢰가 가장 중요하다고 생각합니다. 진정한 소통을 통해 서로의 감정을 이해하고, 함께 성장할 수 있는 관계를 원합니다. 일상 속 작은 것에도 감사하며, 서로의 꿈과 목표를 지지하고 응원하는 파트너가 되고 싶습니다. 또한, 유머와 즐거움을 잃지 않으며, 함께하는 순간들을 소중히 여기고 싶습니다. 사랑은 서로를 더 나은 사람으로 만들어주는 힘이 있다고 믿습니다. 서로에게 긍정적인 영향을 주며 행복한 시간을 함께하고 싶습니다!"
                )
            )
        )
    }
}

@Preview
@Composable
private fun ProfileValuePickBodyPreview() {
    PieceTheme {
        ProfileValuePickBody(
            nickName = "nickName",
            selfDescription = "selfDescription",
            pickCards = listOf(
                ValuePick(
                    category = "음주",
                    question = "사귀는 사람과 함께 술을 마시는 것을 좋아하나요?",
                    option1 = "함께 술을 즐기고 싶어요",
                    option2 = "같이 술을 즐길 수 없어도 괜찮아요",
                    isSimilarToMe = true,
                ),
                ValuePick(
                    category = "만남 빈도",
                    question = "주말에 얼마나 자주 데이트를 하고싶나요?",
                    option1 = "주말에는 최대한 같이 있고 싶어요",
                    option2 = "하루 정도는 각자 보내고 싶어요",
                    isSimilarToMe = true,
                ),
                ValuePick(
                    category = "연락 빈도",
                    question = "연인 사이에 얼마나 자주 연락하는게 좋은가요?",
                    option1 = "바빠도 최대한 자주 연락하고 싶어요",
                    option2 = "연락은 생각날 때만 종종 해도 괜찮아요",
                    isSimilarToMe = true,
                ),
                ValuePick(
                    category = "연락 방식",
                    question = "연락할 때 어떤 방법을 더 좋아하나요?",
                    option1 = "전화보다는 문자나 카톡이 좋아요",
                    option2 = "문자나 카톡보다는 전화가 좋아요",
                    isSimilarToMe = true,
                )
            ),
            onRefuseClick = {},
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


