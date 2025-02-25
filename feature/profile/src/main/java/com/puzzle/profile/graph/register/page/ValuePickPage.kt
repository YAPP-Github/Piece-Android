package com.puzzle.profile.graph.register.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceChip
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.profile.graph.register.contract.RegisterProfileState
import com.puzzle.profile.graph.register.model.ValuePickRegisterRO

@Composable
internal fun ValuePickPage(
    valuePicks: List<ValuePickRegisterRO>,
    onValuePickContentChange: (List<ValuePickRegisterRO>) -> Unit,
) {
    var isContentEdited: Boolean by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PieceTheme.colors.white),
    ) {
        ValuePickCards(
            valuePicks = valuePicks,
            onContentChange = {
                val updatedValuePicks = valuePicks.map { valuePick ->
                    if (valuePick.id == it.id) {
                        it
                    } else {
                        valuePick
                    }
                }

                isContentEdited = updatedValuePicks != valuePicks
                onValuePickContentChange(updatedValuePicks)
            },
        )
    }
}

@Composable
private fun ValuePickCards(
    valuePicks: List<ValuePickRegisterRO>,
    onContentChange: (ValuePickRegisterRO) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            Text(
                text = stringResource(R.string.value_pick_profile_page_header),
                style = PieceTheme.typography.headingLSB,
                color = PieceTheme.colors.black,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .padding(horizontal = 20.dp),
            )

            Text(
                text = stringResource(R.string.value_pick_profile_page_sub_header),
                style = PieceTheme.typography.bodySM,
                color = PieceTheme.colors.dark3,
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 16.dp)
                    .padding(horizontal = 20.dp),
            )
        }

        itemsIndexed(valuePicks) { idx, item ->
            ValuePickCard(
                item = item,
                onContentChange = onContentChange,
                modifier = Modifier.padding(
                    horizontal = 20.dp,
                    vertical = 24.dp,
                )
            )

            if (idx < valuePicks.size - 1) {
                HorizontalDivider(
                    thickness = 12.dp,
                    color = PieceTheme.colors.light3,
                    modifier = Modifier.fillMaxWidth(),
                )
            } else {
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}

@Composable
private fun ValuePickCard(
    item: ValuePickRegisterRO,
    onContentChange: (ValuePickRegisterRO) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.padding(bottom = 12.dp)) {
            Image(
                painter = painterResource(R.drawable.ic_question_default),
                contentDescription = "질문",
                colorFilter = ColorFilter.tint(PieceTheme.colors.primaryDefault),
                modifier = Modifier
                    .size(20.dp)
                    .padding(start = 4.dp),
            )

            Text(
                text = item.category,
                style = PieceTheme.typography.bodySSB,
                color = PieceTheme.colors.primaryDefault,
                modifier = Modifier.padding(start = 6.dp),
            )
        }

        Text(
            text = item.question,
            style = PieceTheme.typography.headingMSB,
            color = PieceTheme.colors.dark1,
            modifier = Modifier.padding(bottom = 24.dp),
        )

        item.answerOptions.forEachIndexed { index, answer ->
            PieceChip(
                label = answer.content,
                selected = answer.number == item.selectedAnswer,
                onChipClicked = {
                    onContentChange(
                        item.copy(selectedAnswer = answer.number)
                    )
                },
                modifier = if (index < item.answerOptions.size - 1) {
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                } else {
                    Modifier.fillMaxWidth()
                },
            )
        }
    }
}

@Preview
@Composable
private fun ValueTalkPagePreview() {
    PieceTheme {
        ValuePickPage(
            valuePicks = RegisterProfileState().valuePicks,
            onValuePickContentChange = {},
        )
    }
}
