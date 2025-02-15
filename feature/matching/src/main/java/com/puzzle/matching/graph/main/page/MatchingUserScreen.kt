package com.puzzle.matching.graph.main.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.puzzle.common.ui.verticalScrollbar
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceMainTopBar
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.domain.model.match.MatchInfo
import com.puzzle.domain.model.match.MatchStatus
import com.puzzle.domain.model.match.MatchStatus.BEFORE_OPEN
import com.puzzle.domain.model.match.MatchStatus.GREEN_LIGHT
import com.puzzle.domain.model.match.MatchStatus.MATCHED
import com.puzzle.domain.model.match.MatchStatus.RESPONDED
import com.puzzle.domain.model.match.MatchStatus.WAITING

@Composable
internal fun MatchingUserScreen(
    isNotificationEnabled: Boolean,
    matchInfo: MatchInfo,
    remainTime: String,
    onButtonClick: () -> Unit,
    onMatchingDetailClick: () -> Unit,
) {
    val listState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PieceTheme.colors.black)
            .padding(horizontal = 20.dp),
    ) {
        PieceMainTopBar(
            title = stringResource(R.string.matching_title),
            textStyle = PieceTheme.typography.branding,
            titleColor = PieceTheme.colors.white,
            rightComponent = {
                if (isNotificationEnabled) {
                    Image(
                        painter = painterResource(R.drawable.ic_alarm_black),
                        contentDescription = "알람",
                        colorFilter = ColorFilter.tint(PieceTheme.colors.white),
                        modifier = Modifier.size(32.dp),
                    )
                }
            },
            modifier = Modifier.padding(bottom = 20.dp),
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(PieceTheme.colors.white.copy(alpha = 0.1f)),
        ) {
            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.precious_connection_start))
                    withStyle(style = SpanStyle(color = PieceTheme.colors.subDefault)) {
                        append(remainTime)
                    }
                    append(stringResource(R.string.time_remaining))
                },
                style = PieceTheme.typography.bodySM,
                color = PieceTheme.colors.light1,
                modifier = Modifier.padding(vertical = 12.dp),
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .padding(top = 8.dp, bottom = 30.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(PieceTheme.colors.white)
                .padding(20.dp),
        ) {
            MatchStatusRow(matchInfo.matchStatus)

            Column {
                Text(
                    text = matchInfo.description,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = PieceTheme.typography.headingLSB,
                    color = PieceTheme.colors.black,
                )

                Text(
                    text = stringResource(R.string.nickname_format, matchInfo.nickname),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = PieceTheme.typography.headingLSB,
                    color = PieceTheme.colors.black,
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 12.dp),
                ) {
                    Text(
                        text = stringResource(
                            R.string.value_count_format,
                            matchInfo.matchedValueCount
                        ),
                        style = PieceTheme.typography.bodyMM,
                        color = PieceTheme.colors.dark2,
                    )

                    VerticalDivider(
                        thickness = 1.dp,
                        color = PieceTheme.colors.light2,
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = matchInfo.location,
                        style = PieceTheme.typography.bodyMM,
                        color = PieceTheme.colors.dark2,
                    )

                    VerticalDivider(
                        thickness = 1.dp,
                        color = PieceTheme.colors.light2,
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = matchInfo.job,
                        style = PieceTheme.typography.bodyMM,
                        color = PieceTheme.colors.dark2,
                    )
                }
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = PieceTheme.colors.light2,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = stringResource(R.string.same_value_as_me),
                        style = PieceTheme.typography.bodyMM,
                        color = PieceTheme.colors.black,
                    )

                    Text(
                        text = "${matchInfo.matchedValueCount}개",
                        style = PieceTheme.typography.bodyMM,
                        color = PieceTheme.colors.primaryDefault,
                    )
                }

                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth()
                        .weight(1f)
                        .heightIn(max = 191.dp)
                        .verticalScrollbar(
                            state = listState,
                            color = PieceTheme.colors.light2
                        ),
                ) {
                    items(
                        items = matchInfo.matchedValueList,
                        key = { it },
                    ) { value -> ValueTag(value) }

                    item {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(20.dp),
                        )
                    }
                }

                val label = when (matchInfo.matchStatus) {
                    BEFORE_OPEN -> stringResource(R.string.check_matching_pieces)
                    WAITING, GREEN_LIGHT -> stringResource(R.string.accept_matching)
                    RESPONDED -> stringResource(R.string.responded)
                    MATCHED -> stringResource(R.string.check_contact)
                    else -> ""
                }
                PieceSolidButton(
                    label = label,
                    enabled = matchInfo.matchStatus != RESPONDED,
                    onClick = onButtonClick,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun MatchStatusRow(
    matchStatus: MatchStatus,
) {
    val (imageRes, tag, description) = when (matchStatus) {
        BEFORE_OPEN -> Triple(
            R.drawable.ic_matching_loading,
            stringResource(R.string.before_open),
            stringResource(R.string.check_the_matching_pieces)
        )

        WAITING -> Triple(
            R.drawable.ic_matching_loading,
            stringResource(R.string.waiting_for_response),
            stringResource(R.string.please_respond_to_matching)
        )

        RESPONDED -> Triple(
            R.drawable.ic_matching_check,
            stringResource(R.string.responded),
            stringResource(R.string.waiting_for_other_response)
        )

        GREEN_LIGHT -> Triple(
            R.drawable.ic_matching_heart,
            stringResource(R.string.green_light),
            stringResource(R.string.other_accepted_matching)
        )

        MATCHED -> Triple(
            R.drawable.ic_matching_check,
            stringResource(R.string.matched),
            stringResource(R.string.connected_with_other)
        )

        else -> Triple(
            R.drawable.ic_matching_loading,
            stringResource(R.string.before_open),
            stringResource(R.string.check_the_matching_pieces)
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
        )

        Text(
            text = tag,
            style = PieceTheme.typography.bodySSB,
            color = PieceTheme.colors.dark2
        )

        Text(
            text = description,
            style = PieceTheme.typography.bodySM,
            color = PieceTheme.colors.dark3
        )
    }
}

@Composable
private fun ValueTag(value: String) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(4.dp))
            .background(PieceTheme.colors.primaryLight)
            .padding(vertical = 6.dp, horizontal = 12.dp),
    ) {
        Text(
            text = value,
            style = PieceTheme.typography.bodyMR,
            color = PieceTheme.colors.black,
        )
    }
}

@Preview
@Composable
private fun PreviewMatchingUserScreen() {
    PieceTheme {
        MatchingUserScreen(
            isNotificationEnabled = true,
            matchInfo = MatchInfo(
                matchId = 1,
                matchStatus = WAITING,
                description = "음악과 요리를 좋아하는",
                nickname = "수줍은 수달",
                birthYear = "02",
                location = "광주광역시",
                job = "학생",
                matchedValueCount = 7,
                matchedValueList = listOf(
                    "바깥 데이트 스킨십도 가능",
                    "함께 술을 즐기고 싶어요",
                    "커밍아웃은 가까운 친구에게만 했어요",
                )
            ),
            onButtonClick = {},
            onMatchingDetailClick = {},
            remainTime = " 00:00:00 "
        )
    }
}

@Preview
@Composable
private fun PreviewMatchingUserScreen2() {
    PieceTheme {
        MatchingUserScreen(
            isNotificationEnabled = true,
            matchInfo = MatchInfo(
                matchId = 1,
                matchStatus = RESPONDED,
                description = "음악과 요리를 좋아하는",
                nickname = "수줍은 수달",
                birthYear = "02",
                location = "광주광역시",
                job = "학생",
                matchedValueCount = 7,
                matchedValueList = listOf(
                    "바깥 데이트 스킨십도 가능",
                    "함께 술을 즐기고 싶어요",
                    "커밍아웃은 가까운 친구에게만 했어요",
                )
            ),
            onButtonClick = {},
            onMatchingDetailClick = {},
            remainTime = " 00:00:00 "
        )
    }
}

@Preview
@Composable
private fun PreviewMatchingUserScreen3() {
    PieceTheme {
        MatchingUserScreen(
            isNotificationEnabled = true,
            matchInfo = MatchInfo(
                matchId = 1,
                matchStatus = GREEN_LIGHT,
                description = "음악과 요리를 좋아하는",
                nickname = "수줍은 수달",
                birthYear = "02",
                location = "광주광역시",
                job = "학생",
                matchedValueCount = 7,
                matchedValueList = listOf(
                    "바깥 데이트 스킨십도 가능",
                    "함께 술을 즐기고 싶어요",
                    "커밍아웃은 가까운 친구에게만 했어요",
                )
            ),
            onButtonClick = {},
            onMatchingDetailClick = {},
            remainTime = " 00:00:00 "
        )
    }
}

@Preview
@Composable
private fun PreviewMatchingUserScreen4() {
    PieceTheme {
        MatchingUserScreen(
            isNotificationEnabled = true,
            matchInfo = MatchInfo(
                matchId = 1,
                matchStatus = BEFORE_OPEN,
                description = "음악과 요리를 좋아하는",
                nickname = "수줍은 수달",
                birthYear = "02",
                location = "광주광역시",
                job = "학생",
                matchedValueCount = 7,
                matchedValueList = listOf(
                    "바깥 데이트 스킨십도 가능",
                    "함께 술을 즐기고 싶어요",
                    "커밍아웃은 가까운 친구에게만 했어요",
                )
            ),
            onButtonClick = {},
            onMatchingDetailClick = {},
            remainTime = " 00:00:00 ",
        )
    }
}
