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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceSubTopBar
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.matching.detail.contract.MatchingDetailIntent
import com.puzzle.matching.detail.contract.MatchingDetailState
import com.puzzle.matching.detail.contract.MatchingDetailState.BasicInfoState
import java.time.LocalDate

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
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
    ) {
        PieceSubTopBar(
            title = state.currentPage.title,
            onNavigationClick = { },
            rightComponent = {
                Image(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = "오른쪽 버튼",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { onCloseClick() },
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
        )

        MatchingDetailContent(
            state = state,
            onMoreClick = onMoreClick,
            modifier = Modifier.weight(1f),
        )

        MatchingDetailBottomBar(
            onShowPicturesClick = onBackPageClick,
            onConfirmClick = onNextPageClick,
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

// TODO(Top bar 변경되면 삭제)
@Composable
private fun MatchingDetailTopBar(
    showBackButton: Boolean,
    onBackClick: () -> Unit,
    onCloseClick: () -> Unit,
    title: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showBackButton) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                )
            }
            Text(text = title)
        } else {
            Spacer(modifier = Modifier.size(48.dp))
        }

        IconButton(onClick = onCloseClick) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
            )
        }
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
                    state = state.basicInfoState,
                    onMoreClick = onMoreClick,
                )
            }

            MatchingDetailState.MatchingDetailPage.ValueTalkState -> {
                ProfileValueTalkBody(state.valueTalkState)
            }

            MatchingDetailState.MatchingDetailPage.ValuePickState -> {
                ProfileValuePickBody(state.valuePickState)
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
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_left_disable),
            contentDescription = "이전 페이지 버튼",
            modifier = modifier
                .size(52.dp)
                .clickable {
                    onShowPicturesClick()
                },
        )

        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_right_able),
            contentDescription = "다음 페이지 버튼",
            modifier = modifier
                .size(52.dp)
                .clickable {
                    onConfirmClick()
                },
        )
    }
}

@Composable
private fun ProfileBasicInfoBody(
    state: BasicInfoState,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentYear = remember { LocalDate.now().year }
    val ageText = remember(state.birthYear, currentYear) { state.calculateAge(currentYear)}

    Column(modifier = modifier) {
        BasicInfoName(
            onMoreClick = onMoreClick,
            modifier = Modifier
                .padding(vertical = 20.dp)
                .weight(1f),
        )

        BasicInfoCard(
            ageText = ageText,
            birthYear = state.birthYear,
            height = state.height,
            religion = state.religion,
            activityRegion = state.activityRegion,
            occupation = state.occupation,
            smokeStatue = state.smokeStatue,
        )
    }
}

@Composable
private fun BasicInfoName(
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
            text = "나를 표현하는 한 마디",
            style = PieceTheme.typography.bodyMR,
            color = PieceTheme.colors.black,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "오늘의 매칭 조각",
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
    ageText: String,
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
            horizontalArrangement = Arrangement.spacedBy(5.5.dp),
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
                            text = ageText,
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
                // TODO(이 부분은 디자이너님한테 물어보고 수정 및 상수화 필요)
                modifier = modifier.weight((120 / 66f)),
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
                modifier = modifier.weight((120 / 66f)),
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

// TODO(아래는 다음 이슈부터 작업)
@Composable
private fun ProfileValueTalkBody(
    state: MatchingDetailState.ValueTalkState,
) {
    val dummyItems = remember { dummyValueTalkItems() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        items(dummyItems) { item ->
            ValueTalkCard(item)
        }
    }
}

data class ValueTalkItem(val title: String, val description: String)

fun dummyValueTalkItems() = listOf(
    ValueTalkItem("음악 취향", "여행하며 모을 감성, LGBTQ+ 권리를 말해요..."),
    ValueTalkItem("공통 가치관", "요리, 고기, 라이딩 좋아해요..."),
    ValueTalkItem("연애관", "서로 존중하고 신뢰받으며 성장하는 관계...")
)

@Composable
private fun ValueTalkCard(item: ValueTalkItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = item.title)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = item.description)
        }
    }
}

@Composable
private fun ProfileValuePickBody(
    state: MatchingDetailState.ValuePickState,
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
            state = MatchingDetailState.BasicInfoState(),
            onMoreClick = {},
        )
    }
}

@Preview
@Composable
private fun ProfileValueTalkBodyPreview() {
    PieceTheme {
        ProfileValueTalkBody(MatchingDetailState.ValueTalkState())
    }
}

@Preview
@Composable
private fun ProfileValuePickBodyPreview() {
    PieceTheme {
        ProfileValuePickBody(MatchingDetailState.ValuePickState())
    }
}

