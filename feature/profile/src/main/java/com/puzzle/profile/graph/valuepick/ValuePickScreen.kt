package com.puzzle.profile.graph.valuepick

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.common.ui.repeatOnStarted
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceChip
import com.puzzle.designsystem.component.PieceSubTopBar
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.domain.model.matching.ValuePick
import com.puzzle.profile.graph.valuepick.contract.ValuePickIntent
import com.puzzle.profile.graph.valuepick.contract.ValuePickSideEffect
import com.puzzle.profile.graph.valuepick.contract.ValuePickState
import com.puzzle.profile.graph.valuepick.contract.ValuePickState.ScreenState

@Composable
internal fun ValuePickRoute(
    viewModel: ValuePickViewModel = mavericksViewModel()
) {
    val state by viewModel.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel) {
        lifecycleOwner.repeatOnStarted {
            viewModel.sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is ValuePickSideEffect.Navigate ->
                        viewModel.navigationHelper.navigate(sideEffect.navigationEvent)
                }
            }
        }
    }

    ValuePickScreen(
        state = state,
        onSaveClick = {},
        onBackClick = { viewModel.onIntent(ValuePickIntent.OnBackClick) },
    )
}

@Composable
private fun ValuePickScreen(
    state: ValuePickState,
    onSaveClick: (List<ValuePick>) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var screenState: ScreenState by remember { mutableStateOf(ScreenState.SAVED) }
    var valuePicks: List<ValuePick> by remember { mutableStateOf(state.valuePicks) }
    var isContentEdited: Boolean by remember { mutableStateOf(false) }
    var editedValuePickLabels: List<String> by remember { mutableStateOf(emptyList()) }

    BackHandler {
        if (screenState == ScreenState.EDITING) {
            // TODO : 데이터 초기화
            screenState = ScreenState.SAVED
        } else {
            onBackClick()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PieceTheme.colors.white),
    ) {
        PieceSubTopBar(
            title = when (screenState) {
                ScreenState.SAVED -> stringResource(R.string.value_pick_profile_topbar_title)
                ScreenState.EDITING -> stringResource(R.string.value_pick_edit_profile_topbar_title)
            },
            onNavigationClick = onBackClick,
            rightComponent = {
                when (screenState) {
                    ScreenState.SAVED ->
                        Text(
                            text = stringResource(R.string.value_pick_profile_topbar_edit),
                            style = PieceTheme.typography.bodyMM,
                            color = PieceTheme.colors.primaryDefault,
                            modifier = Modifier.clickable {
                                screenState = ScreenState.EDITING
                            },
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
                            modifier = Modifier.clickable {
                                if (isContentEdited) {
//                                    valuePicks = valuePicks.map { valuePick ->
//                                        if (editedValuePickLabels.contains(valuePick.label)) {
//                                            valuePick.copy(aiSummary = "")
//                                        } else {
//                                            valuePick
//                                        }
//                                    }
                                    onSaveClick(valuePicks)
                                    isContentEdited = false
                                    editedValuePickLabels = emptyList()
                                }
                                screenState = ScreenState.SAVED
                            },
                        )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 14.dp,
                ),
        )

        ValuePickCards(
            valuePicks = state.valuePicks,
            screenState = screenState,
            onContentChange = {

            },
        )
    }
}

@Composable
private fun ValuePickCards(
    valuePicks: List<ValuePick>,
    screenState: ScreenState,
    onContentChange: (ValuePick) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        itemsIndexed(valuePicks) { idx, item ->
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
    item: ValuePick,
    screenState: ScreenState,
    onContentChange: (ValuePick) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.padding(bottom = 12.dp)) {
            Image(
                painter = painterResource(R.drawable.ic_question),
                contentDescription = "질문",
                colorFilter = ColorFilter.tint(PieceTheme.colors.dark3),
                modifier = Modifier
                    .size(20.dp)
                    .padding(start = 4.dp),
            )

            Text(
                text = "음주",
                style = PieceTheme.typography.bodySSB,
                color = PieceTheme.colors.primaryDefault,
                modifier = Modifier.padding(start = 6.dp),
            )
        }

        Text(
            text = "사귀는 사람과 함께 술을 마시는 것을 좋아하나요?",
            style = PieceTheme.typography.headingMSB,
            color = PieceTheme.colors.dark1,
            modifier = Modifier.padding(bottom = 24.dp),
        )

        PieceChip(
            label = "함께 술을 즐기고 싶어요",
            selected = true,
            onChipClicked = {
                onContentChange(
                    item.copy()
                )
            },
            enabled = screenState == ScreenState.EDITING,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
        )

        PieceChip(
            label = "같이 술을 즐길 수 없어도 괜찮아요.",
            selected = true,
            onChipClicked = {
                onContentChange(
                    item.copy()
                )
            },
            enabled = screenState == ScreenState.EDITING,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun ValuePickPreview() {
    PieceTheme {
        ValuePickScreen(
            state = ValuePickState(),
            onBackClick = {},
            onSaveClick = {},
        )
    }
}