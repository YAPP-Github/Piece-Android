package com.puzzle.matching.detail

import androidx.compose.foundation.border
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
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.matching.detail.contract.MatchingDetailIntent
import com.puzzle.matching.detail.contract.MatchingDetailState

@Composable
fun MatchingDetailRoute(
    viewModel: MatchingDetailViewModel = mavericksViewModel()
) {
    val state by viewModel.collectAsState()

    MatchingDetailScreen(
        state = state,
        onCloseClick = { viewModel.onIntent(MatchingDetailIntent.OnMatchingDetailCloseClick) },
        onBackPageClick = { viewModel.onIntent(MatchingDetailIntent.OnBackPageClick) },
        onNextPageClick = { viewModel.onIntent(MatchingDetailIntent.OnNextPageClick) },
    )
}

@Composable
fun MatchingDetailScreen(
    state: MatchingDetailState,
    onCloseClick: () -> Unit,
    onBackPageClick: () -> Unit,
    onNextPageClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        MatchingDetailTopBar(
            showBackButton = state.currentPage !is MatchingDetailState.BasicInfoState,
            onBackClick = onBackPageClick,
            onCloseClick = onCloseClick,
            title = state.currentPage.title,
        )

        MatchingDetailContent(
            currentPage = state.currentPage,
            modifier = modifier.weight(1f),
        )

        MatchingDetailBottomBar(
            onShowPicturesClick = onBackPageClick,
            onConfirmClick = onNextPageClick
        )
    }
}

@Composable
fun MatchingDetailTopBar(
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
                    contentDescription = "Back"
                )
            }
            Text(text = title)
        } else {
            Spacer(modifier = Modifier.size(48.dp))
        }

        IconButton(onClick = onCloseClick) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close"
            )
        }
    }
}


@Composable
fun MatchingDetailContent(
    currentPage: MatchingDetailState.MatchingDetailPage,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (currentPage) {
            is MatchingDetailState.BasicInfoState -> ProfileBasicInfoBody(currentPage)
            is MatchingDetailState.ValuePick -> ProfileValuePickBody(currentPage)
            is MatchingDetailState.ValueTalk -> ProfileValueTalkBody(currentPage)
        }
    }
}

@Composable
fun MatchingDetailBottomBar(
    onShowPicturesClick: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(onClick = onShowPicturesClick) {
            Text("사진 보기")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = onConfirmClick) {
            Text("매칭 수락하기")
        }
    }
}

@Composable
fun ProfileBasicInfoBody(
    state: MatchingDetailState.BasicInfoState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = 20.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 20.dp)
                .weight(1f),
        ) {
            Text(text = "오늘의 매칭 조각")
            Spacer(modifier = modifier.weight(1f))
            Text(text = "나를 표현하는 한 마디")
            Row {
                Text(text = "닉네임", modifier = Modifier.weight(1f))
            }
        }
        Column(
            modifier = Modifier
                .padding(vertical = 12.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.5.dp),
            ) {
                InfoItem(
                    title = "나이",
                    content = state.birthYear,
                    subContent = state.birthYear,
                    modifier = Modifier.weight(1f)
                )
                InfoItem(
                    title = "키",
                    content = state.height,
                    modifier = Modifier.weight(1f)
                )
                InfoItem(
                    title = "종교",
                    content = state.religion,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.5.dp),
            ) {
                InfoItem(
                    title = "활동 지역",
                    content = state.activityRegion,
                    modifier = Modifier.weight(1f)
                )
                InfoItem(
                    title = "직업",
                    content = state.occupation,
                    modifier = Modifier.weight(1f)
                )
                InfoItem(
                    title = "흡연",
                    content = state.smokeStatue,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun InfoItem(
    title: String,
    content: String,
    subContent: String? = null,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
    ) {
        Text(text = title)
        Row {
            Text(text = content, modifier = modifier)
            if (subContent != null) {
                Text(text = subContent, modifier = modifier)
            }
        }
    }
}

@Composable
fun ProfileValueTalkBody(
    state: MatchingDetailState.ValueTalk,
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
fun ValueTalkCard(item: ValueTalkItem) {
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
fun ProfileValuePickBody(
    state: MatchingDetailState.ValuePick
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
fun TabContent(contentText: String) {
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
        )
    }
}

@Preview
@Composable
private fun ProfileBasicInfoBodyPreview() {
    PieceTheme {
        ProfileBasicInfoBody(MatchingDetailState.BasicInfoState())
    }
}

@Preview
@Composable
private fun ProfileValueTalkBodyPreview() {
    PieceTheme {
        ProfileValueTalkBody(MatchingDetailState.ValueTalk())
    }
}

@Preview
@Composable
private fun ProfileValuePickBodyPreview() {
    PieceTheme {
        ProfileValuePickBody(MatchingDetailState.ValuePick())
    }
}

