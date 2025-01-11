package com.puzzle.auth.graph.signup.page

import android.Manifest.permission.CAMERA
import android.Manifest.permission.POST_NOTIFICATIONS
import android.Manifest.permission.READ_CONTACTS
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.component.PieceSubBackTopBar
import com.puzzle.designsystem.component.PieceToggle
import com.puzzle.designsystem.foundation.PieceTheme

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun ColumnScope.AccessRightsPage(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    val permissionList = rememberMultiplePermissionsState(
        listOfNotNull(
            CAMERA,
            if (SDK_INT >= TIRAMISU) POST_NOTIFICATIONS else null,
            READ_CONTACTS,
        )
    )

    val cameraPermission = permissionList.permissions.find { it.permission == CAMERA }
    val notificationPermission = permissionList.permissions.find {
        if (SDK_INT >= TIRAMISU) it.permission == POST_NOTIFICATIONS else true
    }
    val contactsPermission = permissionList.permissions.find { it.permission == READ_CONTACTS }

    LaunchedEffect(permissionList) {
        permissionList.launchMultiplePermissionRequest()
    }

    BackHandler { onBackClick() }

    PieceSubBackTopBar(
        title = "",
        onBackClick = onBackClick,
    )

    Text(
        text = buildAnnotatedString {
            append("편리한 Piece 이용을 위해\n아래 ")

            withStyle(style = SpanStyle(color = PieceTheme.colors.primaryDefault)) {
                append("권한 허용")
            }

            append("이 필요해요")
        },
        style = PieceTheme.typography.headingLSB,
        color = PieceTheme.colors.black,
        modifier = Modifier.padding(top = 20.dp),
    )

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        PiecePermissionRow(
            icon = R.drawable.ic_permission_camera,
            label = "사진,카메라 [필수]",
            description = "프로필 생성 시 사진 첨부를 위해 필요해요.",
            checked = cameraPermission?.status == PermissionStatus.Granted,
            onCheckedChange = {
                if (cameraPermission?.status != PermissionStatus.Granted) {
                    cameraPermission?.launchPermissionRequest()
                }
            },
        )

        PiecePermissionRow(
            icon = R.drawable.ic_permission_alarm,
            label = "알림 [선택]",
            description = "매칭 현황 등 중요 메시지 수신을 위해 필요해요.",
            checked = notificationPermission?.status == PermissionStatus.Granted,
            onCheckedChange = {
                if (notificationPermission?.status != PermissionStatus.Granted) {
                    notificationPermission?.launchPermissionRequest()
                }
            },
        )

        PiecePermissionRow(
            icon = R.drawable.ic_permission_call,
            label = "연락처 [선택]",
            description = "지인을 수집하기 위해 필요해요.",
            checked = contactsPermission?.status == PermissionStatus.Granted,
            onCheckedChange = {
                if (contactsPermission?.status != PermissionStatus.Granted) {
                    contactsPermission?.launchPermissionRequest()
                }
            },
        )
    }

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1.8f),
    )

    PieceSolidButton(
        label = stringResource(R.string.next),
        enabled = cameraPermission?.status == PermissionStatus.Granted,
        onClick = onNextClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 10.dp),
    )
}

@Composable
private fun PiecePermissionRow(
    @DrawableRes icon: Int,
    label: String,
    description: String,
    checked: Boolean,
    onCheckedChange: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange() }
            .padding(vertical = 16.dp)
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(48.dp),
        )

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = label,
                    style = PieceTheme.typography.bodyMM,
                    color = PieceTheme.colors.black,
                    modifier = Modifier.weight(1f),
                )

                PieceToggle(
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                )
            }

            Text(
                text = description,
                style = PieceTheme.typography.bodySM,
                color = PieceTheme.colors.dark2,
            )
        }
    }
}

@Preview
@Composable
private fun AccessRightsPagePreview() {
    PieceTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
        ) {
            AccessRightsPage(
                onBackClick = {},
                onNextClick = {}
            )
        }
    }
}

@Preview
@Composable
private fun PiecePermissionRowPreview() {
    PieceTheme {
        PiecePermissionRow(
            icon = R.drawable.ic_permission_camera,
            label = "사진,카메라 [필수]",
            description = "프로필 생성 시 사진 첨부를 위해 필요해요.",
            checked = true,
            onCheckedChange = {}
        )
    }
}
