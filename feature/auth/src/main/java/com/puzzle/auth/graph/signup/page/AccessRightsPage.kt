package com.puzzle.auth.graph.signup.page

import android.Manifest.permission.POST_NOTIFICATIONS
import android.Manifest.permission.READ_CONTACTS
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale
import com.puzzle.common.ui.clickable
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
    onDisEnabledButtonClick: () -> Unit,
) {
    val context = LocalContext.current
    val permissionList = rememberMultiplePermissionsState(
        listOfNotNull(
            when {
                SDK_INT < 33 -> READ_EXTERNAL_STORAGE
                SDK_INT == 33 -> READ_MEDIA_IMAGES
                else -> READ_MEDIA_VISUAL_USER_SELECTED
            },
            if (SDK_INT >= TIRAMISU) POST_NOTIFICATIONS else null,
            READ_CONTACTS
        )
    )
    val galleryPermission = permissionList.permissions
        .find {
            it.permission == when {
                SDK_INT < 33 -> READ_EXTERNAL_STORAGE
                SDK_INT == 33 -> READ_MEDIA_IMAGES
                else -> READ_MEDIA_VISUAL_USER_SELECTED
            }
        }
    val notificationPermission = permissionList.permissions
        .find { if (SDK_INT >= TIRAMISU) it.permission == POST_NOTIFICATIONS else true }
    val contactsPermission = permissionList.permissions
        .find { it.permission == READ_CONTACTS }

    BackHandler { onBackClick() }

    LaunchedEffect(permissionList) {
        permissionList.launchMultiplePermissionRequest()
    }

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
            label = stringResource(R.string.permission_gallery),
            description = stringResource(R.string.permission_gallery_description),
            checked = galleryPermission?.status == PermissionStatus.Granted,
            onCheckedChange = { handlePermission(context, galleryPermission) },
        )

        PiecePermissionRow(
            icon = R.drawable.ic_permission_alarm,
            label = stringResource(R.string.permission_notification),
            description = stringResource(R.string.permission_notification_description),
            checked = notificationPermission?.status == PermissionStatus.Granted,
            onCheckedChange = { handlePermission(context, notificationPermission) },
        )

        PiecePermissionRow(
            icon = R.drawable.ic_permission_call,
            label = stringResource(R.string.permission_contacts),
            description = stringResource(R.string.permission_contacts_description),
            checked = contactsPermission?.status == PermissionStatus.Granted,
            onCheckedChange = { handlePermission(context, contactsPermission) },
        )
    }

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1.8f),
    )

    val isButtonEnabled = galleryPermission?.status == PermissionStatus.Granted
    Box {
        PieceSolidButton(
            label = stringResource(R.string.next),
            enabled = isButtonEnabled,
            onClick = onNextClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 10.dp),
        )

        if (!isButtonEnabled) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clickable { onDisEnabledButtonClick() }
            )
        }
    }
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
                    style = PieceTheme.typography.bodyMSB,
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

@OptIn(ExperimentalPermissionsApi::class)
private fun handlePermission(context: Context, permission: PermissionState?) {
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
                onNextClick = {},
                onDisEnabledButtonClick = {},
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
            label = "사진 [필수]",
            description = "프로필 생성 시 사진 첨부를 위해 필요해요.",
            checked = true,
            onCheckedChange = {},
        )
    }
}
