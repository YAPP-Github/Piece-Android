package com.puzzle.presentation.update

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.puzzle.designsystem.R
import com.puzzle.designsystem.component.PieceDialog
import com.puzzle.designsystem.component.PieceDialogDefaultTop
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.domain.model.configure.ForceUpdate
import com.puzzle.presentation.BuildConfig

@Composable
internal fun ForceUpdateDialog(forceUpdate: ForceUpdate?) {
    val context = LocalContext.current
    var isDialogVisible by remember { mutableStateOf(false) }

    LaunchedEffect(forceUpdate) {
        isDialogVisible = isShowForceUpdateDialog(context, forceUpdate)
    }

    if (isDialogVisible) {
        PieceDialog(
            onDismissRequest = {},
            dialogTop = {
                PieceDialogDefaultTop(
                    title = stringResource(R.string.update_title),
                    subText = stringResource(R.string.update_subtext),
                )
            },
            dialogBottom = {
                PieceSolidButton(
                    label = stringResource(R.string.update_app),
                    onClick = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            BuildConfig.PIECE_MARKET_URL.toUri()
                        )
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp, top = 12.dp),
                )
            },
        )
    }
}

private fun isShowForceUpdateDialog(
    context: Context,
    info: ForceUpdate?,
): Boolean {
    if (info == null) return false

    val currentVersion = context.packageManager.getPackageInfo(context.packageName, 0).versionName
    return checkShouldUpdate(currentVersion!!, info.minVersion)
}

private fun checkShouldUpdate(currentVersion: String, minVersion: String): Boolean {
    val current = normalizeVersion(currentVersion)
    val min = normalizeVersion(minVersion)
    return (0..2).any { current[it] < min[it] }
}

private fun normalizeVersion(version: String): List<Int> = version.split('.')
    .map { it.toIntOrNull() ?: 0 }
    .let { if (it.size == 2) it + 0 else it }
