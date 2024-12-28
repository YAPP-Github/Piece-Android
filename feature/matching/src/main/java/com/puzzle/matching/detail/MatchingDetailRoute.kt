package com.puzzle.matching.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.designsystem.R
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
        onBackPageClick = { viewModel.onIntent(MatchingDetailIntent.OnBackPageClick) },
        onNextPageClick = { viewModel.onIntent(MatchingDetailIntent.OnNextPageClick) },
        onMoreClick = { viewModel.onIntent(MatchingDetailIntent.OnMoreClick) },
    )
}

@Composable
private fun MatchingDetailScreen(
    state: MatchingDetailState,
    onCloseClick: () -> Unit,
    onBackPageClick: () -> Unit,
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
                .padding(horizontal = 20.dp)
                .align(Alignment.TopCenter)
                .let {
                    if (state.currentPage != MatchingDetailPage.BasicInfoState) {
                        it.background(PieceTheme.colors.white)
                    } else {
                        it
                    }
                },
        )

        MatchingDetailBottomBar(
            onShowPicturesClick = onBackPageClick,
            onConfirmClick = onNextPageClick,
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
                    onMoreClick = onMoreClick,
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
    onShowPicturesClick: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_left_disable),
            contentDescription = "이전 페이지 버튼",
            modifier = Modifier
                .size(52.dp)
                .clickable {
                    onShowPicturesClick()
                },
        )

        Spacer(modifier = Modifier.width(8.dp))

        Image(
            painter = painterResource(id = R.drawable.ic_right_able),
            contentDescription = "다음 페이지 버튼",
            modifier = Modifier
                .size(52.dp)
                .clickable {
                    onConfirmClick()
                },
        )
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
            text = "오늘의 매칭 조각",
            style = PieceTheme.typography.bodyMM,
            color = PieceTheme.colors.primaryDefault,
        )

        Spacer(modifier = Modifier.weight(1f))

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
                title = "나이",
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "만",
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
                            text = "세",
                            style = PieceTheme.typography.bodySM,
                            color = PieceTheme.colors.black,
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "${birthYear}년생",
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
                title = "키",
                content = height,
                modifier = modifier.weight(1f),
            )
            InfoItem(
                title = "종교",
                content = religion,
                modifier = modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(5.5.dp),
        ) {
            InfoItem(
                title = "활동 지역",
                content = activityRegion,
                modifier = modifier.size(
                    width = 144.dp,
                    height = 80.dp,
                ),
            )

            InfoItem(
                title = "직업",
                content = occupation,
                modifier = modifier.weight(1f),
            )

            InfoItem(
                title = "흡연",
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
            .background(Color.White)
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
    val valueTalkHeaderHeight = 105.dp

    val valueTalkHeaderHeightPx = with(LocalDensity.current) { valueTalkHeaderHeight.roundToPx() }

    val connection = remember(valueTalkHeaderHeightPx) {
        CollapsingHeaderNestedScrollConnection(valueTalkHeaderHeightPx)
    }

    val density = LocalDensity.current
    val spaceHeight by remember(density) {
        derivedStateOf {
            with(density) {
                (valueTalkHeaderHeightPx + connection.headerOffset).toDp()
            }
        }
    }

    Box(
        modifier = modifier
            .nestedScroll(connection)
    ) {
        Column {
            Spacer(
                Modifier.height(spaceHeight)
            )
            LazyColumn(
                contentPadding = PaddingValues(top = valueTalkHeaderHeight),
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

        ValueTalkHeader(
            nickName = nickName,
            selfDescription = selfDescription,
            onMoreClick = onMoreClick,
            modifier = Modifier
                .offset { IntOffset(0, connection.headerOffset) },
        )

        Spacer(
            modifier = modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(PieceTheme.colors.light2)
                .align(Alignment.TopCenter),
        )
    }
}

private class CollapsingHeaderNestedScrollConnection(
    val headerHeight: Int
) : NestedScrollConnection {

    var headerOffset: Int by mutableIntStateOf(0)
        private set

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val delta = available.y.toInt()
        val newOffset = headerOffset + delta
        val previousOffset = headerOffset
        headerOffset = newOffset.coerceIn(-headerHeight, 0)
        val consumed = headerOffset - previousOffset
        return Offset(0f, consumed.toFloat())
    }
}

@Composable
private fun ValueTalkHeader(
    nickName: String,
    selfDescription: String,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color.White)
            .padding(
                vertical = 20.dp,
                horizontal = 20.dp
            ),
    ) {
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
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
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
    pickCards: List<ValuePick>,
) {
    val tabIndex = remember { mutableIntStateOf(0) }

    val tabTitles = listOf("전체", "나와 같은", "나와 다른")

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        TabRow(
            selectedTabIndex = tabIndex.intValue,
            modifier = Modifier.fillMaxWidth(),
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = (tabIndex.intValue == index),
                    onClick = { tabIndex.intValue = index },
                    text = { Text(text = title) },
                )
            }
        }

        when (tabIndex.intValue) {
            0 -> TabContent("전체 탭의 내용 예시...")
            1 -> TabContent("나만 탭의 내용 예시...")
            2 -> TabContent("너만 탭의 내용 예시...")
        }
    }
}

@Composable
private fun TabContent(contentText: String) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        items(15) { index ->
            Text(
                text = "$contentText 아이템 $index",
                modifier = Modifier.padding(vertical = 8.dp),
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
            pickCards = emptyList()
        )
    }
}

