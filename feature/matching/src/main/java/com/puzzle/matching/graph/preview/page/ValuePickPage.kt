package com.puzzle.matching.graph.preview.page

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
import androidx.compose.foundation.lazy.items
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
import com.puzzle.designsystem.component.PieceChip
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.domain.model.profile.MyValuePick
import com.puzzle.matching.graph.detail.common.component.BasicInfoHeader

@Composable
internal fun ValuePickPage(
    nickName: String,
    selfDescription: String,
    pickCards: List<MyValuePick>,
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

    Box(modifier = modifier.nestedScroll(connection)) {
        Column {
            Spacer(Modifier.height(spaceHeight))

            ValuePickCards(
                pickCards = pickCards,
                modifier = Modifier.fillMaxSize(),
            )
        }

        BasicInfoHeader(
            nickName = nickName,
            selfDescription = selfDescription,
            onMoreClick = { },
            hasMoreButton = false,
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
private fun ValuePickCards(
    pickCards: List<MyValuePick>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(PieceTheme.colors.light3)
            .padding(horizontal = 20.dp),
    ) {
        items(
            items = pickCards,
            key = { item -> item.id },
        ) { item ->
            ValuePickCard(
                item = item,
                modifier = Modifier.padding(top = 20.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
private fun ValuePickCard(
    item: MyValuePick,
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
                painter = painterResource(id = R.drawable.ic_question_default),
                contentDescription = "basic info 배경화면",
                modifier = Modifier
                    .size(20.dp),
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = item.category,
                style = PieceTheme.typography.bodySSB,
                color = PieceTheme.colors.primaryDefault,
            )
        }

        Text(
            text = item.question,
            style = PieceTheme.typography.headingMSB,
            color = PieceTheme.colors.dark1,
            modifier = Modifier.padding(top = 12.dp, bottom = 24.dp),
        )

        item.answerOptions.forEachIndexed { idx, answer ->
            PieceChip(
                label = answer.content,
                selected = answer.number == item.selectedAnswer,
                onChipClicked = {},
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
            pickCards = emptyList(),
        )
    }
}
