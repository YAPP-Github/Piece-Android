package com.puzzle.matching.graph.detail.page

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.puzzle.common.ui.CollapsingHeaderNestedScrollConnection
import com.puzzle.common.ui.clickable
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceSubButton
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.domain.model.profile.Answer
import com.puzzle.domain.model.profile.OpponentValuePick
import com.puzzle.matching.graph.detail.common.component.BasicInfoHeader

@Composable
internal fun ValuePickPage(
    nickName: String,
    selfDescription: String,
    pickCards: List<OpponentValuePick>,
    onDeclineClick: () -> Unit,
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
        stringResource(R.string.valuepick_all),
        stringResource(R.string.valuepick_same),
        stringResource(R.string.valuepick_different),
    )

    Box(modifier = modifier.nestedScroll(connection)) {
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
                    onDeclineClick = onDeclineClick,
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
    pickCards: List<OpponentValuePick>,
    onDeclineClick: () -> Unit,
) {
    AnimatedContent(
        targetState = tabIndex,
        transitionSpec = {
            fadeIn(tween(700)) togetherWith fadeOut(tween(700))
        },
        modifier = Modifier.fillMaxSize(),
    ) {
        when (it) {
            0 -> ValuePickCards(
                pickCards = pickCards,
                onDeclineClick = onDeclineClick,
            )

            1 -> ValuePickCards(
                pickCards = pickCards.filter { it.isSameWithMe },
                onDeclineClick = onDeclineClick,
            )

            2 -> ValuePickCards(
                pickCards = pickCards.filterNot { it.isSameWithMe },
                onDeclineClick = onDeclineClick,
            )
        }
    }
}

@Composable
private fun ValuePickCards(
    pickCards: List<OpponentValuePick>,
    onDeclineClick: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(PieceTheme.colors.light3)
            .padding(horizontal = 20.dp),
    ) {
        itemsIndexed(pickCards) { idx, item ->
            ValuePickCard(
                valuePickQuestion = item,
                modifier = Modifier.padding(top = 20.dp)
            )
        }

        item {
            Text(
                text = stringResource(R.string.valuepick_refuse),
                style = PieceTheme.typography.bodyMM.copy(
                    textDecoration = TextDecoration.Underline
                ),
                color = PieceTheme.colors.dark3,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 60.dp)
                    .clickable { onDeclineClick() },
            )
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
        indicator = { tabPositions ->
            if (tabIndex < tabPositions.size) {
                TabRowDefaults.SecondaryIndicator(
                    color = PieceTheme.colors.black,
                    modifier = Modifier.tabIndicatorOffset(tabPositions[tabIndex]),
                )
            }
        },
        divider = {},
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
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
    valuePickQuestion: OpponentValuePick,
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
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(28.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_question),
                contentDescription = "basic info 배경화면",
                modifier = Modifier
                    .size(20.dp),
            )

            Spacer(modifier = modifier.width(6.dp))

            Text(
                text = valuePickQuestion.category,
                style = PieceTheme.typography.bodySSB,
                color = PieceTheme.colors.primaryDefault,
            )

            Spacer(modifier = modifier.weight(1f))

            if (valuePickQuestion.isSameWithMe) {
                Text(
                    text = stringResource(R.string.valuepick_similar),
                    style = PieceTheme.typography.captionM,
                    color = PieceTheme.colors.subDefault,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(23.dp))
                        .background(PieceTheme.colors.subLight)
                        .padding(vertical = 6.dp, horizontal = 12.dp),
                )
            }
        }

        Text(
            text = valuePickQuestion.question,
            style = PieceTheme.typography.headingMSB,
            color = PieceTheme.colors.dark1,
            modifier = Modifier.padding(top = 12.dp, bottom = 24.dp),
        )

        valuePickQuestion.answers.forEachIndexed { idx, answer ->
            PieceSubButton(
                label = answer.content,
                onClick = {},
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (idx != 0) Modifier.padding(top = 8.dp)
                        else Modifier
                    ),
            )
        }
    }
}

@Preview
@Composable
private fun ProfileValuePickPagePreview() {
    PieceTheme {
        ValuePickPage(
            nickName = "nickName",
            selfDescription = "selfDescription",
            pickCards = listOf(
                OpponentValuePick(
                    category = "음주",
                    question = "사귀는 사람과 함께 술을 마시는 것을 좋아하나요?",
                    answers = listOf(
                        Answer(1, "함께 술을 즐기고 싶어요"),
                        Answer(2, "같이 술을 즐길 수 없어도 괜찮아요")
                    ),
                    selectedAnswer = 1,
                    isSameWithMe = true,
                ),
                OpponentValuePick(
                    category = "만남 빈도",
                    question = "주말에 얼마나 자주 데이트를 하고싶나요?",
                    answers = listOf(
                        Answer(1, "주말에는 최대한 같이 있고 싶어요"),
                        Answer(2, "하루 정도는 각자 보내고 싶어요")
                    ),
                    selectedAnswer = 1,
                    isSameWithMe = false,
                ),
                OpponentValuePick(
                    category = "연락 빈도",
                    question = "연인 사이에 얼마나 자주 연락하는게 좋은가요?",
                    answers = listOf(
                        Answer(1, "바빠도 최대한 자주 연락하고 싶어요"),
                        Answer(2, "연락은 생각날 때만 종종 해도 괜찮아요")
                    ),
                    selectedAnswer = 1,
                    isSameWithMe = true,
                ),
                OpponentValuePick(
                    category = "연락 방식",
                    question = "연락할 때 어떤 방법을 더 좋아하나요?",
                    answers = listOf(
                        Answer(1, "전화보다는 문자나 카톡이 좋아요"),
                        Answer(2, "문자나 카톡보다는 전화가 좋아요")
                    ),
                    selectedAnswer = 1,
                    isSameWithMe = false,
                ),
            ),
            onDeclineClick = {},
        )
    }
}
