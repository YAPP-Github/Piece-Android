@file:OptIn(ExperimentalPermissionsApi::class)

package com.puzzle.setting.graph.main

import android.Manifest.permission.READ_CONTACTS
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.puzzle.common.ui.clickable
import com.puzzle.common.ui.throttledClickable
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceDialog
import com.puzzle.designsystem.component.PieceDialogBottom
import com.puzzle.designsystem.component.PieceDialogDefaultTop
import com.puzzle.designsystem.component.PieceMainTopBar
import com.puzzle.designsystem.component.PieceToggle
import com.puzzle.designsystem.foundation.PieceTheme
import com.puzzle.domain.model.auth.OAuthProvider
import com.puzzle.setting.graph.main.contract.SettingIntent
import com.puzzle.setting.graph.main.contract.SettingState
import kotlinx.coroutines.launch


@Composable
internal fun SettingRoute(
    viewModel: SettingViewModel = mavericksViewModel(),
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(viewModel) {
        val version = getVersionInfo(
            context = context,
            onError = { scope.launch { viewModel.errorHelper.sendError(error = it) } },
        )
        viewModel.setAppVersion(version?.let { "v$it" } ?: "")
    }

    SettingScreen(
        state = state,
        onWithdrawClick = { viewModel.onIntent(SettingIntent.OnWithdrawClick) },
        onLogoutClick = { viewModel.onIntent(SettingIntent.OnLogoutClick) },
        onNoticeClick = { viewModel.onIntent(SettingIntent.OnNoticeClick) },
        onPrivacyAndPolicyClick = { viewModel.onIntent(SettingIntent.OnPrivacyAndPolicyClick) },
        onTermsOfUseClick = { viewModel.onIntent(SettingIntent.OnTermsOfUseClick) },
        onInquiryClick = { viewModel.onIntent(SettingIntent.OnInquiryClick) },
        onUpdatePushNotification = { viewModel.onIntent(SettingIntent.UpdatePushNotification) },
        onUpdateMatchNotification = { viewModel.onIntent(SettingIntent.UpdateMatchNotification) },
        onUpdateBlockAcquaintances = { viewModel.onIntent(SettingIntent.UpdateBlockAcquaintances) },
        onRefreshClick = {
            val phoneNumbers = readContactPhoneNumbers(context)
            viewModel.onIntent(SettingIntent.OnRefreshClick(phoneNumbers))
        },
    )
}

@Composable
private fun SettingScreen(
    state: SettingState,
    onWithdrawClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onNoticeClick: () -> Unit,
    onPrivacyAndPolicyClick: () -> Unit,
    onTermsOfUseClick: () -> Unit,
    onInquiryClick: () -> Unit,
    onUpdatePushNotification: () -> Unit,
    onUpdateMatchNotification: () -> Unit,
    onUpdateBlockAcquaintances: () -> Unit,
    onRefreshClick: () -> Unit,
) {
    var isLogoutDialogShow by remember { mutableStateOf(false) }

    if (isLogoutDialogShow) {
        PieceDialog(
            onDismissRequest = { isLogoutDialogShow = false },
            dialogTop = {
                PieceDialogDefaultTop(
                    title = stringResource(R.string.setting_logout),
                    subText = stringResource(R.string.setting_logout_description),
                )
            },
            dialogBottom = {
                PieceDialogBottom(
                    leftButtonText = stringResource(R.string.cancel),
                    rightButtonText = stringResource(R.string.confirm),
                    onLeftButtonClick = { isLogoutDialogShow = false },
                    onRightButtonClick = { onLogoutClick() },
                )
            },
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PieceTheme.colors.white)
    ) {
        PieceMainTopBar(
            title = stringResource(R.string.setting_screen),
            textStyle = PieceTheme.typography.branding,
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        HorizontalDivider(
            color = PieceTheme.colors.light2,
            thickness = 1.dp,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
        ) {
            if (state.isNotificationEnabled) {
                NotificationBody(
                    isMatchingNotificationEnabled = state.isMatchingNotificationEnabled,
                    isPushNotificationEnabled = state.isPushNotificationEnabled,
                    onMatchingNotificationCheckedChange = onUpdateMatchNotification,
                    onPushNotificationCheckedChange = onUpdatePushNotification,
                )
            }

            SystemSettingBody(
                isContactBlocked = state.isContactBlocked,
                lastRefreshTime = state.lastRefreshTime,
                isLoadingContactBlocked = state.isLoadingContactsBlocked,
                onContactBlockedCheckedChange = onUpdateBlockAcquaintances,
                onRefreshClick = onRefreshClick,
            )

            InquiryBody(onContactUsClick = onInquiryClick)

            AnnouncementBody(
                version = state.version,
                onNoticeClick = onNoticeClick,
                onPrivacyPolicy = onPrivacyAndPolicyClick,
                onTermsClick = onTermsOfUseClick,
            )

            OthersBody(onLogoutClick = { isLogoutDialogShow = true })

            Text(
                text = stringResource(R.string.withdraw_page),
                style = PieceTheme.typography.bodyMM.copy(
                    textDecoration = TextDecoration.Underline
                ),
                color = PieceTheme.colors.dark3,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp, bottom = 60.dp)
                    .throttledClickable(2000L) { onWithdrawClick() },
            )
        }
    }
}

@Composable
private fun NotificationBody(
    isMatchingNotificationEnabled: Boolean,
    isPushNotificationEnabled: Boolean,
    onMatchingNotificationCheckedChange: () -> Unit,
    onPushNotificationCheckedChange: () -> Unit,
) {
    Text(
        text = stringResource(R.string.setting_notification),
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
            text = stringResource(R.string.setting_match_notification),
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
            text = stringResource(R.string.setting_push_notification),
            style = PieceTheme.typography.headingSSB,
            color = PieceTheme.colors.dark1,
            modifier = Modifier.weight(1f),
        )

        PieceToggle(
            checked = isPushNotificationEnabled,
            onCheckedChange = onPushNotificationCheckedChange,
        )
    }

    HorizontalDivider(
        modifier = Modifier.padding(vertical = 16.dp),
        thickness = 1.dp,
        color = PieceTheme.colors.light2
    )
}

@Composable
private fun SystemSettingBody(
    isContactBlocked: Boolean,
    lastRefreshTime: String,
    isLoadingContactBlocked: Boolean,
    onContactBlockedCheckedChange: () -> Unit,
    onRefreshClick: () -> Unit,
) {
    Text(
        text = stringResource(R.string.setting_system),
        style = PieceTheme.typography.bodySM,
        color = PieceTheme.colors.dark2,
        modifier = Modifier.padding(bottom = 8.dp),
    )

    val context = LocalContext.current
    val contactsPermission = rememberPermissionState(READ_CONTACTS)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 17.dp),
    ) {
        Text(
            text = stringResource(R.string.setting_block_contacts),
            style = PieceTheme.typography.headingSSB,
            color = PieceTheme.colors.dark1,
            modifier = Modifier.weight(1f),
        )

        PieceToggle(
            checked = contactsPermission.status == PermissionStatus.Granted && isContactBlocked,
            onCheckedChange = {
                if (contactsPermission.status == PermissionStatus.Granted) {
                    onContactBlockedCheckedChange()
                } else {
                    handlePermission(context, contactsPermission)
                }
            },
        )
    }

    AnimatedVisibility(
        visible = (contactsPermission.status == PermissionStatus.Granted && isContactBlocked),
        enter = fadeIn() + slideInVertically(),
        exit = shrinkOut() + slideOutVertically(),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 16.dp),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.setting_sync_contacts),
                    style = PieceTheme.typography.headingSSB,
                    color = PieceTheme.colors.dark1,
                    modifier = Modifier.padding(bottom = 8.dp),
                )

                Text(
                    text = stringResource(R.string.setting_update_contacts),
                    style = PieceTheme.typography.captionM,
                    color = PieceTheme.colors.dark3,
                )

                Text(
                    text = stringResource(R.string.setting_block_new_contacts),
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
                        text = stringResource(R.string.setting_last_refresh),
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

            if (isLoadingContactBlocked) {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.anim_setting_loading))
                val progress by animateLottieCompositionAsState(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                )
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.ic_refresh),
                    contentDescription = "초기화",
                    modifier = Modifier
                        .padding(start = 11.dp)
                        .throttledClickable(2000L) {
                            if (contactsPermission.status == PermissionStatus.Granted) {
                                onRefreshClick()
                            } else {
                                handlePermission(context, contactsPermission)
                            }
                        },
                )
            }
        }
    }

    HorizontalDivider(
        color = PieceTheme.colors.light2,
        thickness = 1.dp,
        modifier = Modifier.padding(vertical = 16.dp)
    )
}

@OptIn(ExperimentalPermissionsApi::class)
internal fun handlePermission(context: Context, permission: PermissionState?) {
    permission?.let {
        if (it.status == PermissionStatus.Granted || !it.status.shouldShowRationale) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
            }
            context.startActivity(intent)
        } else {
            it.launchPermissionRequest()
        }
    }
}

@Composable
private fun InquiryBody(onContactUsClick: () -> Unit) {
    Text(
        text = stringResource(R.string.setting_inquiry),
        style = PieceTheme.typography.bodySM,
        color = PieceTheme.colors.dark2,
        modifier = Modifier.padding(bottom = 8.dp),
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 17.dp)
            .clickable { onContactUsClick() },
    ) {
        Text(
            text = stringResource(R.string.setting_contact_us),
            style = PieceTheme.typography.headingSSB,
            color = PieceTheme.colors.dark1,
            modifier = Modifier.weight(1f),
        )

        Image(
            painter = painterResource(R.drawable.ic_arrow_right),
            contentDescription = "상세 내용",
            modifier = Modifier.padding(start = 4.dp),
        )
    }

    HorizontalDivider(
        color = PieceTheme.colors.light2,
        thickness = 1.dp,
        modifier = Modifier.padding(vertical = 16.dp)
    )
}


@Composable
private fun AnnouncementBody(
    version: String,
    onNoticeClick: () -> Unit,
    onPrivacyPolicy: () -> Unit,
    onTermsClick: () -> Unit,
) {
    Text(
        text = stringResource(R.string.setting_guidance),
        style = PieceTheme.typography.bodySM,
        color = PieceTheme.colors.dark2,
        modifier = Modifier.padding(bottom = 8.dp),
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 17.dp)
            .clickable { onNoticeClick() },
    ) {
        Text(
            text = stringResource(R.string.setting_announcement),
            style = PieceTheme.typography.headingSSB,
            color = PieceTheme.colors.dark1,
            modifier = Modifier.weight(1f),
        )

        Image(
            painter = painterResource(R.drawable.ic_arrow_right),
            contentDescription = "상세 내용",
            modifier = Modifier.padding(start = 4.dp),
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 17.dp)
            .clickable { onPrivacyPolicy() },
    ) {
        Text(
            text = stringResource(R.string.setting_privacy_policy),
            style = PieceTheme.typography.headingSSB,
            color = PieceTheme.colors.dark1,
            modifier = Modifier.weight(1f),
        )

        Image(
            painter = painterResource(R.drawable.ic_arrow_right),
            contentDescription = "상세 내용",
            modifier = Modifier.padding(start = 4.dp),
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 17.dp)
            .clickable { onTermsClick() },
    ) {
        Text(
            text = stringResource(R.string.setting_term),
            style = PieceTheme.typography.headingSSB,
            color = PieceTheme.colors.dark1,
            modifier = Modifier.weight(1f),
        )

        Image(
            painter = painterResource(R.drawable.ic_arrow_right),
            contentDescription = "상세 내용",
            modifier = Modifier.padding(start = 4.dp),
        )
    }

    Text(
        text = stringResource(R.string.setting_version, version),
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
private fun OthersBody(onLogoutClick: () -> Unit) {
    Text(
        text = stringResource(R.string.setting_other),
        style = PieceTheme.typography.bodySM,
        color = PieceTheme.colors.dark2,
        modifier = Modifier.padding(bottom = 8.dp),
    )

    Text(
        text = stringResource(R.string.setting_logout),
        style = PieceTheme.typography.headingSSB,
        color = PieceTheme.colors.dark1,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 17.dp)
            .clickable { onLogoutClick() },
    )
}

private fun getVersionInfo(
    context: Context,
    onError: (Exception) -> Unit,
): String? {
    var version: String? = null
    try {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        version = packageInfo.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        onError(e)
    }
    return version
}

private fun readContactPhoneNumbers(context: Context): List<String> {
    val phoneNumbers = mutableListOf<String>()

    val contentResolver = context.contentResolver
    val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
    val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)

    contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
        val numberIndex =
            cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
        while (cursor.moveToNext()) {
            val phoneNumber = cursor.getString(numberIndex).replace(Regex("[^0-9]"), "")
            if (phoneNumber.isNotBlank()) {
                phoneNumbers.add(phoneNumber)
            }
        }
    }

    return phoneNumbers.distinct()
}

@Preview
@Composable
private fun PreviewSettingScreen() {
    PieceTheme {
        SettingScreen(
            state = SettingState(
                oAuthProvider = OAuthProvider.KAKAO,
                email = "example@kakao.com",
                isMatchingNotificationEnabled = true,
                isPushNotificationEnabled = false,
                isContactBlocked = true,
                lastRefreshTime = "MM월 DD일 오전 00:00",
                version = "v1.0.0",
            ),
            onWithdrawClick = {},
            onLogoutClick = {},
            onNoticeClick = {},
            onPrivacyAndPolicyClick = {},
            onTermsOfUseClick = {},
            onInquiryClick = {},
            onUpdatePushNotification = {},
            onUpdateMatchNotification = {},
            onUpdateBlockAcquaintances = {},
            onRefreshClick = {},
        )
    }
}

@Preview
@Composable
private fun PreviewLogoutDialog() {
    PieceTheme {
        PieceDialog(
            onDismissRequest = { },
            dialogTop = {
                PieceDialogDefaultTop(
                    title = "로그아웃",
                    subText = "로그아웃하시겠습니까?",
                )
            },
            dialogBottom = {
                PieceDialogBottom(
                    leftButtonText = "취소",
                    rightButtonText = "확인",
                    onLeftButtonClick = {},
                    onRightButtonClick = {},
                )
            },
        )
    }
}
