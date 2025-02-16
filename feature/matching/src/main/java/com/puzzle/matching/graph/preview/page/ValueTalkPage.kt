package com.puzzle.matching.graph.preview.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.puzzle.common.ui.CollapsingHeaderNestedScrollConnection
import com.puzzle.designsystem.R
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.domain.model.profile.MyValueTalk
import com.puzzle.matching.graph.detail.common.component.BasicInfoHeader

@Composable
internal fun ValueTalkPage(
    nickName: String,
    selfDescription: String,
    talkCards: List<MyValueTalk>,
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
    Box(modifier = modifier.nestedScroll(connection)) {
        // 5) Column: Spacer + LazyColumn을 세로로 배치
        //    헤더가 접힐수록 Spacer의 높이가 줄어들고, 그만큼 리스트가 위로 올라옴
        Column {
            // 5-1) 헤더 높이만큼 Spacer를 줘서 리스트가 '헤더 아래'에서 시작
            //      헤더 offset이 변하면, spaceHeight가 변해 리스트도 따라 위로 올라감
            ValueTalkCards(
                talkCards = talkCards,
                modifier = Modifier.padding(top = spaceHeight),
            )
        }

        // 6) 실제 헤더 뷰
        //    offset을 통해 y축 이동 (headerOffset이 음수면 위로 올라가며 사라짐)
        BasicInfoHeader(
            nickName = nickName,
            selfDescription = selfDescription,
            hasMoreButton = false,
            modifier = Modifier
                .offset { IntOffset(0, connection.headerOffset) }
                .background(PieceTheme.colors.white)
                .height(valueTalkHeaderHeight)
                .padding(vertical = 20.dp, horizontal = 20.dp),
        )

        HorizontalDivider(
            thickness = 1.dp,
            color = PieceTheme.colors.light2,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
        )
    }
}

@Composable
private fun ValueTalkCards(
    talkCards: List<MyValueTalk>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(PieceTheme.colors.light3)
            .padding(horizontal = 20.dp),
    ) {
        itemsIndexed(talkCards) { idx, item ->
            ValueTalkCard(
                item = item,
                idx = idx,
                modifier = Modifier.padding(top = 20.dp),
            )
        }

        item {
            Spacer(Modifier.height(60.dp))
        }
    }
}

@Composable
private fun ValueTalkCard(
    item: MyValueTalk,
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
            ),
    ) {
        Text(
            text = item.category,
            style = PieceTheme.typography.bodyMM,
            color = PieceTheme.colors.primaryDefault,
            modifier = Modifier.padding(bottom = 24.dp),
        )

        Image(
            painter = painterResource(id = icons[idxInRange]),
            contentDescription = "basic info 배경화면",
            modifier = Modifier.size(60.dp),
        )

        Text(
            text = item.summary,
            style = PieceTheme.typography.headingMSB,
            color = PieceTheme.colors.black,
            modifier = Modifier.padding(top = 24.dp),
        )

        Text(
            text = item.answer,
            style = PieceTheme.typography.bodySM,
            color = PieceTheme.colors.dark2,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}

@Preview
@Composable
private fun ProfileValueTalkPagePreview() {
    PieceTheme {
        ValueTalkPage(
            nickName = "수줍은 수달",
            selfDescription = "음악과 요리를 좋아하는",
            talkCards = emptyList()
        )
    }
}
