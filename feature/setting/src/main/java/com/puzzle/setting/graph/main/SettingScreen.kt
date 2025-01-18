package com.puzzle.setting.graph.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceMainTopBar
import com.puzzle.designsystem.component.PieceToggle
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.setting.graph.main.contract.SettingState

@Composable
internal fun SettingRoute(
    viewModel: SettingViewModel = mavericksViewModel(),
) {
    val state by viewModel.collectAsState()

    SettingScreen(
        state = state
    )
}

@Composable
private fun SettingScreen(
    state: SettingState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PieceTheme.colors.white)
            .padding(horizontal = 20.dp),
    ) {
        PieceMainTopBar(
            title = "Setting",
            modifier = Modifier.padding(vertical = 19.dp)
        )

        Text(
            text = "로그인 계정",
            style = PieceTheme.typography.bodySM,
            color = PieceTheme.colors.dark2,
            modifier = Modifier.padding(top = 20.dp, bottom = 8.dp),
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(top = 20.dp, bottom = 8.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.ic_puzzle1),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp),
            )
            Text(
                text = "example@kakao.com",
                modifier = Modifier
                    .padding(start = 8.dp)
            )
        }

        Divider(
            color = PieceTheme.colors.light2,
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Text(
            text = "알림",
            style = PieceTheme.typography.bodySM,
            color = PieceTheme.colors.dark2,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 17.dp),
        ) {
            Text(
                text = "알림",
                style = PieceTheme.typography.headingSSB,
                color = PieceTheme.colors.dark1,
                modifier = Modifier.weight(1f),
            )

            PieceToggle(
                checked = true,
                onCheckedChange = {},
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 17.dp),
        ) {
            Text(
                text = "푸쉬 알림",
                style = PieceTheme.typography.headingSSB,
                color = PieceTheme.colors.dark1,
                modifier = Modifier.weight(1f),
            )

            PieceToggle(
                checked = true,
                onCheckedChange = {},
            )
        }

        Divider(
            color = PieceTheme.colors.light2,
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Text(
            text = "시스템 설정",
            style = PieceTheme.typography.bodySM,
            color = PieceTheme.colors.dark2,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 17.dp),
        ) {
            Text(
                text = "지인 차단",
                style = PieceTheme.typography.headingSSB,
                color = PieceTheme.colors.dark1,
                modifier = Modifier.weight(1f),
            )

            PieceToggle(
                checked = true,
                onCheckedChange = {},
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = "지인 차단",
                    style = PieceTheme.typography.headingSSB,
                    color = PieceTheme.colors.dark1,
                    modifier = Modifier.padding(bottom = 8.dp),
                )

                Text(
                    text = "내 연락처 목록을 즉시 업데이트합니다.",
                    style = PieceTheme.typography.captionM,
                    color = PieceTheme.colors.dark3,
                )

                Text(
                    text = "연락처에 새로 추가된 지인을 차단할 수 있어요.",
                    style = PieceTheme.typography.captionM,
                    color = PieceTheme.colors.dark3,
                    modifier = Modifier.padding(bottom = 4.dp),
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_time),
                        contentDescription = "시계",
                        modifier = Modifier,
                    )

                    Text(
                        text = "마지막 새로고침",
                        style = PieceTheme.typography.captionM,
                        color = PieceTheme.colors.dark3,
                    )

                    Text(
                        text = "MM월 DD일 오전 00:00",
                        style = PieceTheme.typography.captionM,
                        color = PieceTheme.colors.dark1,
                    )
                }
            }

//            PieceToggle(
//                checked = true,
//                onCheckedChange = {},
//            )
        }
    }
}

@Preview
@Composable
private fun PreviewSettingScreen() {
    PieceTheme {
        SettingScreen(
            state = SettingState()
        )
    }
}