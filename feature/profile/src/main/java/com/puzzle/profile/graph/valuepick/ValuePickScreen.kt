package com.puzzle.profile.graph.valuepick

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.common.ui.clickable
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceChip
import com.puzzle.designsystem.component.PieceSubTopBar
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.domain.model.profile.AnswerOption
import com.puzzle.domain.model.profile.MyValuePick
import com.puzzle.profile.graph.valuepick.contract.ValuePickIntent
import com.puzzle.profile.graph.valuepick.contract.ValuePickState
import com.puzzle.profile.graph.valuepick.contract.ValuePickState.ScreenState

@Composable
internal fun ValuePickRoute(
    viewModel: ValuePickViewModel = mavericksViewModel()
) {
    val state by viewModel.collectAsState()

    ValuePickScreen(
        state = state,
        onSaveClick = { viewModel.onIntent(ValuePickIntent.OnUpdateClick(it)) },
        onEditClick = { viewModel.onIntent(ValuePickIntent.OnEditClick) },
        onBackClick = { viewModel.onIntent(ValuePickIntent.OnBackClick) },
    )
}

@Composable
private fun ValuePickScreen(
    state: ValuePickState,
    onSaveClick: (List<MyValuePick>) -> Unit,
    onEditClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var valuePicks: List<MyValuePick> by remember(state.valuePicks) { mutableStateOf(state.valuePicks) }
    var isContentEdited: Boolean by remember { mutableStateOf(false) }

    BackHandler { onBackClick() }

    LaunchedEffect(state.screenState) {
        if (state.screenState == ScreenState.EDITING) {
            valuePicks = state.valuePicks
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PieceTheme.colors.white),
    ) {
        PieceSubTopBar(
            title = when (state.screenState) {
                ScreenState.NORMAL -> stringResource(R.string.value_pick_profile_topbar_title)
                ScreenState.EDITING -> stringResource(R.string.value_pick_edit_profile_topbar_title)
            },
            onNavigationClick = onBackClick,
            rightComponent = {
                when (state.screenState) {
                    ScreenState.NORMAL ->
                        Text(
                            text = stringResource(R.string.value_pick_profile_topbar_edit),
                            style = PieceTheme.typography.bodyMM,
                            color = PieceTheme.colors.primaryDefault,
                            modifier = Modifier.clickable { onEditClick() },
                        )

                    ScreenState.EDITING ->
                        Text(
                            text = stringResource(R.string.value_pick_profile_topbar_save),
                            style = PieceTheme.typography.bodyMM,
                            color = if (isContentEdited) {
                                PieceTheme.colors.primaryDefault
                            } else {
                                PieceTheme.colors.dark3
                            },
                            modifier = Modifier.clickable(enabled = isContentEdited) {
                                onSaveClick(valuePicks)
                                isContentEdited = false
                            },
                        )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        )

        ValuePickCards(
            valuePicks = valuePicks,
            screenState = state.screenState,
            onContentChange = { changedValuePick ->
                valuePicks = valuePicks.map { valuePick ->
                    if (valuePick.id == changedValuePick.id) {
                        changedValuePick
                    } else {
                        valuePick
                    }
                }

                isContentEdited = valuePicks != state.valuePicks
            },
        )
    }
}

@Composable
private fun ValuePickCards(
    valuePicks: List<MyValuePick>,
    screenState: ScreenState,
    onContentChange: (MyValuePick) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        itemsIndexed(
            items = valuePicks,
            key = { _, item -> item.id },
        ) { idx, item ->
            ValuePickCard(
                item = item,
                screenState = screenState,
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
            }
        }
    }
}

@Composable
private fun ValuePickCard(
    item: MyValuePick,
    screenState: ScreenState,
    onContentChange: (MyValuePick) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.padding(bottom = 12.dp)) {
            Image(
                painter = painterResource(R.drawable.ic_question_default),
                contentDescription = "질문",
                colorFilter = ColorFilter.tint(PieceTheme.colors.primaryDefault),
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(20.dp)
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
                enabled = screenState == ScreenState.EDITING,
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
private fun ValuePickPreview() {
    PieceTheme {
        ValuePickScreen(
            state = ValuePickState(
                valuePicks = listOf(
                    MyValuePick(
                        id = 0,
                        category = "음주",
                        question = "사귀는 사람과 함께 술을 마시는 것을 좋아하나요?",
                        selectedAnswer = 1,
                        answerOptions = listOf(
                            AnswerOption(
                                number = 1,
                                content = "함께 술을 즐기고 싶어요",
                            ),
                            AnswerOption(
                                number = 2,
                                content = "같이 술을 즐길 수 없어도 괜찮아요."
                            )
                        ),
                    ),
                    MyValuePick(
                        id = 1,
                        category = "만남 빈도",
                        question = "주말에 얼마나 자주 데이트를 하고싶나요?",
                        selectedAnswer = 1,
                        answerOptions = listOf(
                            AnswerOption(
                                number = 1,
                                content = "주말에는 최대한 같이 있고 싶어요",
                            ),
                            AnswerOption(
                                number = 2,
                                content = "하루 정도는 각자 보내고 싶어요."
                            )
                        ),
                    ),
                    MyValuePick(
                        id = 2,
                        category = "연락 빈도",
                        question = "연인 사이에 얼마나 자주 연락하는게 좋은가요?",
                        selectedAnswer = 1,
                        answerOptions = listOf(
                            AnswerOption(
                                number = 1,
                                content = "바빠도 최대한 자주 연락하고 싶어요",
                            ),
                            AnswerOption(
                                number = 2,
                                content = "연락은 생각날 때만 종종 해도 괜찮아요"
                            )
                        ),
                    ),
                    MyValuePick(
                        id = 3,
                        category = "연락 방식",
                        question = "연락할 때 어떤 방법을 더 좋아하나요?",
                        selectedAnswer = 1,
                        answerOptions = listOf(
                            AnswerOption(
                                number = 1,
                                content = "전화보다는 문자나 카톡이 좋아요",
                            ),
                            AnswerOption(
                                number = 2,
                                content = "문자나 카톡보다는 전화가 좋아요"
                            )
                        ),
                    ),
                    MyValuePick(
                        id = 4,
                        category = "데이트",
                        question = "공공장소에서 연인 티를 내는 것에 대해 어떻게 생각하나요?",
                        selectedAnswer = 2,
                        answerOptions = listOf(
                            AnswerOption(
                                number = 1,
                                content = "밖에서 연인 티내는건 조금 부담스러워요",
                            ),
                            AnswerOption(
                                number = 2,
                                content = "밖에서도 자연스럽게 연인 티내고 싶어요"
                            )
                        ),
                    ),
                    MyValuePick(
                        id = 5,
                        category = "장거리 연애",
                        question = "장거리 연애에 대해 어떻게 생각하나요?",
                        selectedAnswer = 1,
                        answerOptions = listOf(
                            AnswerOption(
                                number = 1,
                                content = "믿음이 있다면 장거리 연애도 괜찮아요",
                            ),
                            AnswerOption(
                                number = 2,
                                content = "장거리 연애는 조금 힘들어요"
                            )
                        ),
                    ),
                    MyValuePick(
                        id = 6,
                        category = "SNS",
                        question = "연인이 활발한 SNS 활동을 하거나 게스타라면 기분이 어떨 것 같나요?",
                        selectedAnswer = 2,
                        answerOptions = listOf(
                            AnswerOption(
                                number = 1,
                                content = "연인의 SNS 활동, 크게 상관 없어요",
                            ),
                            AnswerOption(
                                number = 2,
                                content = "SNS 활동과 게스타는 조금 불편해요"
                            )
                        ),
                    ),
                )
            ),
            onBackClick = {},
            onEditClick = {},
            onSaveClick = {},
        )
    }
}
