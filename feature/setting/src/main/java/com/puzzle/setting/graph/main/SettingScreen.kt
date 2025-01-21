package com.puzzle.setting.graph.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceMainTopBar
import com.puzzle.designsystem.component.PieceToggle
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.domain.model.auth.OAuthProvider
import com.puzzle.setting.graph.main.contract.SettingIntent
import com.puzzle.setting.graph.main.contract.SettingState

@Composable
internal fun SettingRoute(
    viewModel: SettingViewModel = mavericksViewModel(),
) {
    val state by viewModel.collectAsState()

    SettingScreen(
        state = state,
        onWithdrawClick = { viewModel.onIntent(SettingIntent.OnWithdrawClick) },
    )
}

@Composable
private fun SettingScreen(
    state: SettingState,
    onWithdrawClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PieceTheme.colors.white)
    ) {
        PieceMainTopBar(
            title = "Setting",
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        HorizontalDivider(
            color = PieceTheme.colors.light2,
            thickness = 1.dp,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
        ) {
            LoginAccountBody(
                oAuthProvider = state.oAuthProvider,
                email = state.email,
            )

            NotificationBody(
                isMatchingNotificationEnabled = state.isMatchingNotificationEnabled,
                isPushNotificationEnabled = state.isPushNotificationEnabled,
                onMatchingNotificationCheckedChange = {},
                onPushNotificationCheckedChagne = {},
            )

            SystemSettingBody(
                isContactBlocked = state.isContactBlocked,
                lastRefreshTime = state.lastRefreshTime,
                onContactBlockedCheckedChange = {},
                onRefreshClick = {},
            )

            InquirBody(
                onContactUsClick = {},
            )

            AnnouncementBody(
                version = state.version,
                onAnnouncementClick = {},
                onPrivacyPolicy = {},
                onTermsClick = {},
            )

            OthersBody(onLogoutClick = {})

            Text(
                text = "탈퇴하기",
                style = PieceTheme.typography.bodyMM.copy(
                    textDecoration = TextDecoration.Underline
                ),
                color = PieceTheme.colors.dark3,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp, bottom = 60.dp)
                    .clickable {
                        onWithdrawClick()
                    },
            )
        }
    }
}

@Composable
private fun OthersBody(onLogoutClick: () -> Unit) {
    Text(
        text = "기타",
        style = PieceTheme.typography.bodySM,
        color = PieceTheme.colors.dark2,
        modifier = Modifier.padding(bottom = 8.dp),
    )

    Text(
        text = "로그아웃",
        style = PieceTheme.typography.headingSSB,
        color = PieceTheme.colors.dark1,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 17.dp)
            .clickable { onLogoutClick() },
    )
}

@Composable
private fun AnnouncementBody(
    version: String,
    onAnnouncementClick: () -> Unit,
    onPrivacyPolicy: () -> Unit,
    onTermsClick: () -> Unit,
) {
    Text(
        text = "안내",
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
            text = "공지사항",
            style = PieceTheme.typography.headingSSB,
            color = PieceTheme.colors.dark1,
            modifier = Modifier.weight(1f),
        )

        Image(
            painter = painterResource(R.drawable.ic_arrow_right),
            contentDescription = "상세 내용",
            modifier = Modifier
                .padding(start = 4.dp)
                .clickable { onAnnouncementClick() },
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 17.dp),
    ) {
        Text(
            text = "개인정보처리방침",
            style = PieceTheme.typography.headingSSB,
            color = PieceTheme.colors.dark1,
            modifier = Modifier.weight(1f),
        )

        Image(
            painter = painterResource(R.drawable.ic_arrow_right),
            contentDescription = "상세 내용",
            modifier = Modifier
                .padding(start = 4.dp)
                .clickable { onPrivacyPolicy() },
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 17.dp),
    ) {
        Text(
            text = "이용약관",
            style = PieceTheme.typography.headingSSB,
            color = PieceTheme.colors.dark1,
            modifier = Modifier.weight(1f),
        )

        Image(
            painter = painterResource(R.drawable.ic_arrow_right),
            contentDescription = "상세 내용",
            modifier = Modifier
                .padding(start = 4.dp)
                .clickable { onTermsClick() },
        )
    }

    Text(
        text = "버전 정보 $version",
        style = PieceTheme.typography.headingSSB,
        color = PieceTheme.colors.dark3,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 17.dp),
    )

    HorizontalDivider(
        color = PieceTheme.colors.light2,
        thickness = 1.dp,
        modifier = Modifier.padding(vertical = 16.dp)
    )
}

@Composable
private fun InquirBody(onContactUsClick: () -> Unit) {
    Text(
        text = "문의",
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
            text = "문의하기",
            style = PieceTheme.typography.headingSSB,
            color = PieceTheme.colors.dark1,
            modifier = Modifier.weight(1f),
        )

        Image(
            painter = painterResource(R.drawable.ic_arrow_right),
            contentDescription = "상세 내용",
            modifier = Modifier
                .padding(start = 4.dp)
                .clickable { onContactUsClick() },
        )
    }

    HorizontalDivider(
        color = PieceTheme.colors.light2,
        thickness = 1.dp,
        modifier = Modifier.padding(vertical = 16.dp)
    )
}

@Composable
private fun SystemSettingBody(
    isContactBlocked: Boolean,
    lastRefreshTime: String,
    onContactBlockedCheckedChange: () -> Unit,
    onRefreshClick: () -> Unit,
) {
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
            checked = isContactBlocked,
            onCheckedChange = onContactBlockedCheckedChange,
        )
    }

    if (isContactBlocked) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "연락처 동기화",
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
                        text = lastRefreshTime,
                        style = PieceTheme.typography.captionM,
                        color = PieceTheme.colors.dark1,
                    )
                }
            }

            Image(
                painter = painterResource(R.drawable.ic_refresh),
                contentDescription = "초기화",
                modifier = Modifier
                    .padding(start = 11.dp)
                    .clickable { onRefreshClick() },
            )
        }
    }

    HorizontalDivider(
        color = PieceTheme.colors.light2,
        thickness = 1.dp,
        modifier = Modifier.padding(vertical = 16.dp)
    )
}

@Composable
private fun NotificationBody(
    isMatchingNotificationEnabled: Boolean,
    isPushNotificationEnabled: Boolean,
    onMatchingNotificationCheckedChange: () -> Unit,
    onPushNotificationCheckedChagne: () -> Unit,
) {
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
            text = "매칭 알림",
            style = PieceTheme.typography.headingSSB,
            color = PieceTheme.colors.dark1,
            modifier = Modifier.weight(1f),
        )

        PieceToggle(
            checked = isMatchingNotificationEnabled,
            onCheckedChange = onMatchingNotificationCheckedChange,
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
            checked = isPushNotificationEnabled,
            onCheckedChange = onPushNotificationCheckedChagne,
        )
    }

    HorizontalDivider(
        modifier = Modifier.padding(vertical = 16.dp),
        thickness = 1.dp,
        color = PieceTheme.colors.light2
    )
}

@Composable
private fun LoginAccountBody(
    oAuthProvider: OAuthProvider?,
    email: String,
) {
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
        val oAuthProviderIconId = when (oAuthProvider) {
            OAuthProvider.GOOGLE -> R.drawable.ic_google_login
            OAuthProvider.KAKAO -> R.drawable.ic_kakao_login
            null -> null
        }

        oAuthProviderIconId?.let {
            Image(
                painter = painterResource(it),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp),
            )
        }

        Text(
            text = email,
            modifier = Modifier.padding(start = 8.dp),
        )
    }

    HorizontalDivider(
        color = PieceTheme.colors.light2,
        thickness = 1.dp,
        modifier = Modifier.padding(vertical = 16.dp)
    )
}

@Preview
@Composable
private fun PreviewSettingScreen() {
    PieceTheme {
        SettingScreen(
            state = SettingState(
                isLoading = false,
                oAuthProvider = OAuthProvider.KAKAO,
                email = "example@kakao.com",
                isMatchingNotificationEnabled = true,
                isPushNotificationEnabled = false,
                isContactBlocked = true,
                lastRefreshTime = "MM월 DD일 오전 00:00",
                version = "v1.0",
            ),
            navigate = {},
        )
    }
}