package com.puzzle.matching

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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.common.ui.verticalScrollbar
import com.puzzle.designsystem.component.PieceMainTopBar
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.matching.contract.MatchingIntent
import com.puzzle.matching.contract.MatchingState
import com.puzzle.designsystem.R

@Composable
internal fun MatchingRoute(
    viewModel: MatchingViewModel = mavericksViewModel(),
) {
    val state by viewModel.collectAsState()

    MatchingScreen(
        state = state,
        navigateToMatchingDetail = { viewModel.onIntent(MatchingIntent.NavigateToMatchingDetail) },
    )
}

@Composable
internal fun MatchingScreen(
    state: MatchingState,
    navigateToMatchingDetail: () -> Unit,
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
            titleColor = PieceTheme.colors.white,
            rightComponent = {
                Image(
                    painter = painterResource(R.drawable.ic_alarm),
                    contentDescription = "",
                )
            },
            modifier = Modifier.padding(vertical = 20.dp),
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
                    append("소중한 인연이 시작되기까지 ")

                    withStyle(style = SpanStyle(color = PieceTheme.colors.subDefault)) {
                        append("02:32:75")
                    }

                    append(" 남았어요")
                },
                style = PieceTheme.typography.bodySM,
                color = PieceTheme.colors.light1,
                modifier = Modifier.padding(vertical = 12.dp),
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .wrapContentSize()
                .padding(top = 8.dp, bottom = 20.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(PieceTheme.colors.white)
                .padding(20.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_matching_loading),
                    contentDescription = null,
                )

                Text(
                    text = "오픈 전",
                    style = PieceTheme.typography.bodySSB,
                    color = PieceTheme.colors.dark2
                )

                Text(
                    text = stringResource(R.string.check_the_matching_pieces),
                    style = PieceTheme.typography.bodySM,
                    color = PieceTheme.colors.dark3
                )
            }

            Column {
                Text(
                    text = "음악과 요리를 좋아하는",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = PieceTheme.typography.headingLSB,
                    color = PieceTheme.colors.black,
                )

                Text(
                    text = "수줍은 수달입니다",
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
                        text = "02년생",
                        style = PieceTheme.typography.bodyMM,
                        color = PieceTheme.colors.dark2,
                    )

                    VerticalDivider(
                        thickness = 1.dp,
                        color = PieceTheme.colors.light2,
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = "광주광역시",
                        style = PieceTheme.typography.bodyMM,
                        color = PieceTheme.colors.dark2,
                    )

                    VerticalDivider(
                        thickness = 1.dp,
                        color = PieceTheme.colors.light2,
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = "학생",
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
                        text = "7개",
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
                        .verticalScrollbar(state = listState),
                ) {
                    items(
                        items = mutableListOf(
                            "바깥 데이트 스킨십도 가능",
                            "함께 술을 즐기고 싶어요",
                            "커밍아웃은 가까운 친구에게만 했어요",
                            "연락은 바쁘더라도 자주",
                            "바깥 데이트 스킨십도 가능2",
                            "함께 술을 즐기고 싶어요2",
                            "커밍아웃은 가까운 친구에게만 했어요2",
                            "연락은 바쁘더라도 자주2",
                            "바깥 데이트 스킨십도 가능3",
                            "함께 술을 즐기고 싶어요3",
                            "커밍아웃은 가까운 친구에게만 했어요3",
                            "연락은 바쁘더라도 자주3",
                        ),
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

                PieceSolidButton(
                    label = stringResource(R.string.accept_matching),
                    onClick = { navigateToMatchingDetail() },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                )
            }
        }
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
private fun PreviewMatchingScreen() {
    PieceTheme {
        MatchingScreen(
            state = MatchingState(),
            navigateToMatchingDetail = {},
        )
    }
}
