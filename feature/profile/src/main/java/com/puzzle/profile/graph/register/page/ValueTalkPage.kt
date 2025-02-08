package com.puzzle.profile.graph.register.page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceTextInputLong
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.domain.model.profile.ValueTalk
import com.puzzle.profile.graph.register.contract.RegisterProfileState
import com.puzzle.profile.graph.register.contract.RegisterProfileState.Companion.PAGE_TRANSITION_DURATION
import com.puzzle.profile.graph.register.contract.RegisterProfileState.Companion.TEXT_DISPLAY_DURATION
import kotlinx.coroutines.delay

@Composable
internal fun ValueTalkPage(
    valueTalks: List<ValueTalk>,
    onValueTalkContentChange: (List<ValueTalk>) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isContentEdited: Boolean by remember { mutableStateOf(false) }
    var editedValueTalkLabels: List<String> by remember { mutableStateOf(emptyList()) }

    ValueTalkCards(
        valueTalks = valueTalks,
        onContentChange = { editedValueTalk ->
            val updatedValueTalks = valueTalks.map { valueTalk ->
                if (valueTalk.category == editedValueTalk.category) {
                    if (!editedValueTalkLabels.contains(valueTalk.category)) {
                        editedValueTalkLabels = editedValueTalkLabels + valueTalk.category
                    }
                    valueTalk.copy(answer = editedValueTalk.answer)
                } else {
                    valueTalk
                }
            }
            isContentEdited = updatedValueTalks != valueTalks
            onValueTalkContentChange(updatedValueTalks)
        },
        modifier = modifier,
    )
}

@Composable
private fun ValueTalkCards(
    valueTalks: List<ValueTalk>,
    onContentChange: (ValueTalk) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            Text(
                text = "가치관 Talk,\n당신의 이야기를 들려주세요",
                style = PieceTheme.typography.headingLSB,
                color = PieceTheme.colors.black,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .padding(horizontal = 20.dp),
            )

            Text(
                text = "AI 요약으로 내용을 더 잘 이해할 수 있도록 도와드려요.\n프로필 생성 후, '프로필-가치관 Talk'에서 확인해보세요!",
                style = PieceTheme.typography.bodySM,
                color = PieceTheme.colors.dark3,
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 16.dp)
                    .padding(horizontal = 20.dp),
            )
        }

        itemsIndexed(valueTalks) { idx, item ->
            ValueTalkCard(
                item = item,
                onContentChange = onContentChange,
                modifier = Modifier.padding(
                    horizontal = 20.dp,
                    vertical = 24.dp,
                )
            )

            if (idx < valueTalks.size - 1) {
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
private fun ValueTalkCard(
    item: ValueTalk,
    onContentChange: (ValueTalk) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = item.category,
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
            value = item.answer,
            onValueChange = {
                onContentChange(item.copy(answer = it))
            },
            limit = 300,
            readOnly = false,
            modifier = Modifier.fillMaxWidth()
        )

        HelpMessageRow(
            helpMessages = item.helpMessages,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )
    }
}

@Composable
fun HelpMessageRow(
    helpMessages: List<String>,
    modifier: Modifier = Modifier,
) {
    // 각 Row의 높이는 고정되어 있으므로 고정값 사용
    val rowHeightDp = 26.dp
    val density = LocalDensity.current
    val rowHeightPx = with(density) { rowHeightDp.toPx() }
    // 총 높이 = rowHeightPx * 메시지 개수
    val totalHeightPx = rowHeightPx * helpMessages.size

    // ScrollState를 이용한 자동 스크롤
    val scrollState = rememberScrollState()
    var isHelpMessageVisible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(TEXT_DISPLAY_DURATION)
            val target = scrollState.value + rowHeightPx.toInt()

            if (target >= totalHeightPx.toInt() - rowHeightPx.toInt()) {
                isHelpMessageVisible = false
                delay(1000)
                scrollState.scrollTo(0)
                isHelpMessageVisible = true
            } else {
                scrollState.animateScrollTo(
                    value = target,
                    animationSpec = tween(
                        durationMillis = PAGE_TRANSITION_DURATION,
                        easing = LinearEasing
                    ),
                )
            }
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Text(
            text = stringResource(id = R.string.value_talk_profile_helpmessage_title),
            style = PieceTheme.typography.bodySR,
            color = PieceTheme.colors.subDefault,
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(PieceTheme.colors.subLight)
                .padding(vertical = 4.dp, horizontal = 6.dp),
        )

        AnimatedVisibility(
            visible = isHelpMessageVisible,
            enter = fadeIn(tween(durationMillis = 500, easing = LinearEasing)),
            exit = fadeOut(tween(durationMillis = 500, easing = LinearEasing)),
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .height(rowHeightDp)
                    .verticalScroll(state = scrollState, enabled = false),
            ) {
                helpMessages.forEach { message ->
                    Text(
                        text = message,
                        style = PieceTheme.typography.bodySR,
                        color = PieceTheme.colors.dark2,
                        modifier = Modifier
                            .height(rowHeightDp)
                            .padding(top = 4.dp),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ValueTalkPagePreview() {
    PieceTheme {
        ValueTalkPage(
            valueTalks = RegisterProfileState().valueTalks,
            onValueTalkContentChange = {},
            modifier = Modifier.background(PieceTheme.colors.white)
        )
    }
}


