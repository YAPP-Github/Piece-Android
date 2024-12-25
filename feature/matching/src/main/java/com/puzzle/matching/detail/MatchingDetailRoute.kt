package com.puzzle.matching.detail

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
        onCloseClick = { viewModel.onIntent(MatchingDetailIntent.OnMatchingDetailCloseClick) }
    )
}

@Composable
fun MatchingDetailScreen(
    state: MatchingDetailState,
    onCloseClick: () -> Unit,
) {
    val pageIndex = remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        MatchingDetailTopBar(
            showBackButton = (pageIndex.intValue != 0),
            onBackClick = {
                if (pageIndex.intValue > 0) pageIndex.intValue--
            },
            onCloseClick = onCloseClick,
            title = "가치관 pick",
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            when (pageIndex.intValue) {
                0 -> ProfileBasicInfoBody()
                1 -> ProfileBasicValueBody()
                2 -> ProfileAllValueBody()
            }
        }

        MatchingDetailBottomBar(
            onShowPicturesClick = {
                if (pageIndex.intValue > 0) pageIndex.intValue--
            },
            onConfirmClick = {
                if (pageIndex.intValue < 2) pageIndex.intValue++
            }
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileBasicInfoBody() {
    val userInfo = remember {
        listOf(
            "만 25세 2000년생",
            "키 180cm",
            "종교 상관없음",
            "세종특별자치시",
            "프리랜서",
            "비흡연"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "음악과 요리를 좋아하는")
        Text(text = "수줍은 수달")

        Spacer(modifier = Modifier.height(16.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            userInfo.forEach { info ->
                InfoItem(text = info)
            }
        }
    }
}

@Composable
fun InfoItem(text: String) {
    Box(
        modifier = Modifier
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        Text(text = text)
    }
}

@Composable
fun ProfileBasicValueBody() {
    val dummyItems = remember { dummyBasicValueItems() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        items(dummyItems) { item ->
            BasicValueCard(item)
        }
    }
}

data class BasicValueItem(val title: String, val description: String)

fun dummyBasicValueItems() = listOf(
    BasicValueItem("음악 취향", "여행하며 모을 감성, LGBTQ+ 권리를 말해요..."),
    BasicValueItem("공통 가치관", "요리, 고기, 라이딩 좋아해요..."),
    BasicValueItem("연애관", "서로 존중하고 신뢰받으며 성장하는 관계...")
)

@Composable
fun BasicValueCard(item: BasicValueItem) {
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
fun ProfileAllValueBody() {
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
        )
    }
}

@Preview
@Composable
private fun ProfileBasicInfoBodyPreview() {
    PieceTheme {
        ProfileBasicInfoBody()
    }
}

@Preview
@Composable
private fun ProfileBasicValueBodyPreview() {
    PieceTheme {
        ProfileBasicValueBody()
    }
}

@Preview
@Composable
private fun ProfileAllValueBodyPreview() {
    PieceTheme {
        ProfileAllValueBody()
    }
}

