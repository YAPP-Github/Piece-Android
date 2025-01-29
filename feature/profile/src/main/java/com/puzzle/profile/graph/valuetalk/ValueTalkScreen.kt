package com.puzzle.profile.graph.valuetalk

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.puzzle.designsystem.component.PieceSubTopBar
import com.puzzle.designsystem.component.PieceTextInputAI
import com.puzzle.designsystem.component.PieceTextInputLong
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.domain.model.matching.ValueTalk
import com.puzzle.profile.graph.valuetalk.contract.ValueTalkSideEffect
import com.puzzle.profile.graph.valuetalk.contract.ValueTalkState
import com.puzzle.profile.graph.valuetalk.contract.ValueTalkState.ScreenState

@Composable
internal fun ValueTalkRoute(
    viewModel: ValueTalkViewModel = mavericksViewModel(),
) {
    val state by viewModel.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel) {
        lifecycleOwner.repeatOnStarted {
            viewModel.sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is ValueTalkSideEffect.Navigate ->
                        viewModel.navigationHelper.navigate(sideEffect.navigationEvent)
                }
            }
        }
    }

    ValueTalkScreen(
        state = state,
        onBackClick = {},
        onSaveClick = {},
        onAiSummarySaveClick = {},
    )
}

@Composable
private fun ValueTalkScreen(
    state: ValueTalkState,
    onSaveClick: (List<ValueTalk>) -> Unit,
    onBackClick: () -> Unit,
    onAiSummarySaveClick: (ValueTalk) -> Unit,
    modifier: Modifier = Modifier,
) {
    var screenState: ScreenState by remember { mutableStateOf(ScreenState.SAVED) }
    var valueTalks: List<ValueTalk> by remember { mutableStateOf(state.valueTalks) }
    var isContentEdited: Boolean by remember { mutableStateOf(false) }

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
                ScreenState.SAVED -> stringResource(R.string.value_talk_profile_topbar_title)
                ScreenState.EDITING -> stringResource(R.string.value_talk_edit_profile_topbar_title)
            },
            onNavigationClick = onBackClick,
            rightComponent = {
                when (screenState) {
                    ScreenState.SAVED ->
                        Text(
                            text = stringResource(R.string.value_talk_profile_topbar_edit),
                            style = PieceTheme.typography.bodyMM,
                            color = PieceTheme.colors.primaryDefault,
                            modifier = Modifier.clickable {
                                screenState = ScreenState.EDITING
                            },
                        )

                    ScreenState.EDITING ->
                        Text(
                            text = stringResource(R.string.value_talk_profile_topbar_save),
                            style = PieceTheme.typography.bodyMM,
                            color = if (isContentEdited) {
                                PieceTheme.colors.primaryDefault
                            } else {
                                PieceTheme.colors.dark3
                            },
                            modifier = Modifier.clickable {
                                if (isContentEdited) {
                                    valueTalks = valueTalks.map { it.copy(aiSummary = "") }
                                    onSaveClick(valueTalks)
                                    isContentEdited = false
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

        ValueTalkCards(
            valueTalks = valueTalks,
            screenState = screenState,
            onContentChange = { editedValueTalk ->
                valueTalks = valueTalks.map { valueTalk ->
                    if (valueTalk.label == editedValueTalk.label) {
                        valueTalk.copy(content = editedValueTalk.content)
                    } else {
                        valueTalk
                    }
                }
                isContentEdited = valueTalks != state.valueTalks
            },
            onAiSummarySaveClick = onAiSummarySaveClick,
        )
    }
}

@Composable
private fun ValueTalkCards(
    valueTalks: List<ValueTalk>,
    screenState: ScreenState,
    onContentChange: (ValueTalk) -> Unit,
    onAiSummarySaveClick: (ValueTalk) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        itemsIndexed(valueTalks) { idx, item ->
            ValueTalkCard(
                item = item,
                screenState = screenState,
                onContentChange = onContentChange,
                onAiSummarySaveClick = onAiSummarySaveClick,
                modifier = Modifier.padding(
                    horizontal = 20.dp,
                    vertical = 24.dp,
                )
            )
        }
    }
}

@Composable
private fun ValueTalkCard(
    item: ValueTalk,
    screenState: ScreenState,
    onContentChange: (ValueTalk) -> Unit,
    onAiSummarySaveClick: (ValueTalk) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = item.label,
            style = PieceTheme.typography.bodySSB,
            color = PieceTheme.colors.primaryDefault,
            modifier = Modifier.padding(bottom = 6.dp),
        )

        Text(
            text = item.title,
            style = PieceTheme.typography.headingMSB,
            color = PieceTheme.colors.dark1,
            modifier = Modifier.padding(bottom = 20.dp),
        )

        PieceTextInputLong(
            value = item.content,
            onValueChange = {
                onContentChange(item.copy(content = it))
            },
            limit = 300,
            readOnly = when (screenState) {
                ScreenState.SAVED -> true
                ScreenState.EDITING -> false
            },
        )

        when (screenState) {
            ScreenState.SAVED ->
                AiSummaryContent(
                    item = item,
                    onAiSummarySaveClick = onAiSummarySaveClick,
                )

            ScreenState.EDITING ->
                HelpMessageContent(helpMessage = item.helpMessage)
        }
    }
}

@Composable
private fun AiSummaryContent(
    item: ValueTalk,
    onAiSummarySaveClick: (ValueTalk) -> Unit
) {
    var editableAiSummary: String by remember { mutableStateOf(item.aiSummary) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
    ) {
        Text(
            text = stringResource(R.string.value_talk_profile_aisummary_title),
            style = PieceTheme.typography.bodySSB,
            color = PieceTheme.colors.primaryDefault,
        )

        Image(
            painter = painterResource(R.drawable.ic_info),
            contentDescription = "정보",
            colorFilter = ColorFilter.tint(PieceTheme.colors.dark3),
            modifier = Modifier
                .size(20.dp)
                .padding(start = 4.dp),
        )
    }

    PieceTextInputAI(
        value = editableAiSummary,
        onValueChange = {
            editableAiSummary = it
        },
        onSaveClick = {
            onAiSummarySaveClick(
                item.copy(
                    content = item.content,
                    aiSummary = it,
                )
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
    )
}

@Composable
private fun HelpMessageContent(
    helpMessage: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .height(26.dp),
    ) {
        Text(
            text = stringResource(R.string.value_talk_profile_helpmessage_title),
            style = PieceTheme.typography.bodySR,
            color = PieceTheme.colors.subDefault,
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(PieceTheme.colors.subLight)
                .padding(
                    vertical = 4.dp,
                    horizontal = 6.dp,
                ),
        )

        Text(
            text = helpMessage,
            style = PieceTheme.typography.bodySR,
            color = PieceTheme.colors.dark2,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}

@Preview
@Composable
private fun ValueTalkPreview() {
    PieceTheme {
        ValueTalkScreen(
            state = ValueTalkState(
                valueTalks = listOf(
                    ValueTalk(
                        label = "연애관",
                        title = "어떠한 사람과 어떠한 연애를 하고 싶은지 들려주세요",
                        content = "저는 연애에서 서로의 존중과 신뢰가 가장 중요하다고 생각합니다. 진정한 소통을 통해 서로의 감정을 이해하고, 함께 성장할 수 있는 관계를 원합니다. 일상 속 작은 것에도 감사하며, 서로의 꿈과 목표를 지지하고 응원하는 파트너가 되고 싶습니다. 또한, 유머와 즐거움을 잃지 않으며, 함께하는 순간들을 소중히 여기고 싶습니다. 사랑은 서로를 더 나은 사람으로 만들어주는 힘이 있다고 믿습니다. 서로에게 긍정적인 영향을 주며 행복한 시간을 함께하고 싶습니다!",
                        aiSummary = "신뢰하며, 함께 성장하고 싶어요.",
                        helpMessage = "어떤 데이트를 즐기고 싶나요?",
                    ),
                    ValueTalk(
                        label = "관심사와 취향",
                        title = "무엇을 할 때 가장 행복한가요?\n요즘 어떠한 것에 관심을 두고 있나요?",
                        content = "저는 다양한 취미와 관심사를 가진 사람입니다. 음악을 사랑하여 콘서트에 자주 가고, 특히 인디 음악과 재즈에 매력을 느낍니다. 요리도 좋아해 새로운 레시피에 도전하는 것을 즐깁니다. 여행을 통해 새로운 맛과 문화를 경험하는 것도 큰 기쁨입니다. 또, 자연을 사랑해서 주말마다 하이킹이나 캠핑을 자주 떠납니다. 영화와 책도 좋아해, 좋은 이야기와 감동을 나누는 시간을 소중히 여깁니다. 서로의 취향을 공유하며 즐거운 시간을 보낼 수 있기를 기대합니다!",
                        aiSummary = "음악, 요리, 하이킹을 좋아해요.",
                        helpMessage = "최근에 가장 행복했던 경험을 공유해 주세요",
                    ),
                    ValueTalk(
                        label = "꿈과 목표",
                        title = "어떤 일을 하며 무엇을 목표로 살아가나요?\n인생에서 이루고 싶은 꿈은 무엇인가요?",
                        content = "안녕하세요! 저는 삶의 매 순간을 소중히 여기며, 꿈과 목표를 이루기 위해 노력하는 사람입니다. 제 가장 큰 꿈은 여행을 통해 다양한 문화와 사람들을 경험하고, 그 과정에서 얻은 지혜를 나누는 것입니다. 또한, LGBTQ+ 커뮤니티를 위한 긍정적인 변화를 이끌어내고 싶습니다. 내가 이루고자 하는 목표는 나 자신을 발전시키고, 사랑하는 사람들과 함께 행복한 순간들을 만드는 것입니다. 서로의 꿈을 지지하며 함께 성장할 수 있는 관계를 기대합니다!",
                        aiSummary = "여행하며 LGBTQ+ 변화를 원해요.",
                        helpMessage = "당신의 직업은 무엇인가요?",
                    )
                )
            ),
            onBackClick = {},
            onSaveClick = {},
            onAiSummarySaveClick = {},
        )
    }
}