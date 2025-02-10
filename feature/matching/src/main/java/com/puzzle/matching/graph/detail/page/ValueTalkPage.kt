package com.puzzle.matching.graph.detail.page

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
import com.puzzle.domain.model.profile.OpponentValueTalk
import com.puzzle.domain.model.profile.ValueTalkQuestion
import com.puzzle.matching.graph.detail.common.component.BasicInfoHeader

@Composable
internal fun ValueTalkPage(
    nickName: String,
    selfDescription: String,
    talkCards: List<OpponentValueTalk>,
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
        modifier = modifier.nestedScroll(connection)
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
                .padding(vertical = 20.dp, horizontal = 20.dp),
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
private fun ValueTalkCards(talkCards: List<OpponentValueTalk>) {
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

@Composable
private fun ValueTalkCard(
    item: OpponentValueTalk,
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
            text = item.category,
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
            text = item.summary,
            style = PieceTheme.typography.headingMSB,
            color = PieceTheme.colors.black,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = item.answer,
            style = PieceTheme.typography.bodySM,
            color = PieceTheme.colors.dark2,
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
            onMoreClick = {},
            talkCards = listOf(
                OpponentValueTalk(
                    category = "꿈과 목표",
                    summary = "여행하며 문화 경험, LGBTQ+ 변화를 원해요.",
                    answer = "안녕하세요! 저는 삶의 매 순간을 소중히 여기며, 꿈과 목표를 이루기 위해 노력하는 사람입니다. 제 가장 큰 꿈은 여행을 통해 다양한 문화와 사람들을 경험하고, 그 과정에서 얻은 지혜를 나누는 것입니다. 또한, LGBTQ+ 커뮤니티를 위한 긍정적인 변화를 이끌어내고 싶습니다. 내가 이루고자 하는 목표는 나 자신을 발전시키고, 사랑하는 사람들과 함께 행복한 순간들을 만드는 것입니다. 서로의 꿈을 지지하며 함께 성장할 수 있는 관계를 기대합니다!",
                ),
                OpponentValueTalk(
                    category = "관심사와 취향",
                    summary = "음악, 요리, 하이킹을 좋아해요.",
                    answer = "저는 다양한 취미와 관심사를 가진 사람입니다. 음악을 사랑하여 콘서트에 자주 가고, 특히 인디 음악과 재즈에 매력을 느낍니다. 요리도 좋아해 새로운 레시피에 도전하는 것을 즐깁니다. 여행을 통해 새로운 맛과 문화를 경험하는 것도 큰 기쁨입니다. 또, 자연을 사랑해서 주말마다 하이킹이나 캠핑을 자주 떠납니다. 영화와 책도 좋아해, 좋은 이야기와 감동을 나누는 시간을 소중히 여깁니다. 서로의 취향을 공유하며 즐거운 시간을 보낼 수 있기를 기대합니다!",
                ),
                OpponentValueTalk(
                    category = "연애관",
                    summary = "서로 존중하고 신뢰하며, 함께 성장하는 관계를 원해요. ",
                    answer = "저는 연애에서 서로의 존중과 신뢰가 가장 중요하다고 생각합니다. 진정한 소통을 통해 서로의 감정을 이해하고, 함께 성장할 수 있는 관계를 원합니다. 일상 속 작은 것에도 감사하며, 서로의 꿈과 목표를 지지하고 응원하는 파트너가 되고 싶습니다. 또한, 유머와 즐거움을 잃지 않으며, 함께하는 순간들을 소중히 여기고 싶습니다. 사랑은 서로를 더 나은 사람으로 만들어주는 힘이 있다고 믿습니다. 서로에게 긍정적인 영향을 주며 행복한 시간을 함께하고 싶습니다!"
                )
            )
        )
    }
}
