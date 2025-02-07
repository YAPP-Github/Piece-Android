package com.puzzle.designsystem.component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.AccessibilityManager
import androidx.compose.ui.platform.LocalAccessibilityManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.puzzle.designsystem.R
import com.puzzle.designsystem.foundation.PieceTheme
import kotlinx.coroutines.delay

@Composable
fun PieceSnackBar(
    snackBarData: SnackbarData,
) {
    val (snackBarType, message) = snackBarData.visuals.message
        .split("/")
        .let { parts ->
            when {
                parts.size >= 2 -> SnackBarType.create(parts[1]) to parts[0]
                else -> SnackBarType.TextOnly to snackBarData.visuals.message
            }
        }

    val image = when (snackBarType) {
        SnackBarType.TextOnly -> null
        SnackBarType.Info -> R.drawable.ic_info
        SnackBarType.Matching -> R.drawable.ic_toast_matching
    }

    val backgroundColor = when (snackBarType) {
        SnackBarType.TextOnly, SnackBarType.Info -> PieceTheme.colors.dark3
        SnackBarType.Matching -> PieceTheme.colors.primaryDefault
    }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val snackBarPosition = when (snackBarType) {
        SnackBarType.TextOnly, SnackBarType.Info -> 82.dp
        SnackBarType.Matching -> (screenHeight.value * (7f / 10f)).dp - 36.dp
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(bottom = snackBarPosition)
            .wrapContentSize()
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(horizontal = 20.dp, vertical = 8.dp),
    ) {
        if (image != null) {
            Image(
                painter = painterResource(image),
                colorFilter = if (snackBarType == SnackBarType.Info) ColorFilter.tint(PieceTheme.colors.white)
                else null,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp),
            )
        }

        Text(
            text = message,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = PieceTheme.typography.bodySM,
            color = PieceTheme.colors.white,
        )
    }
}

enum class SnackBarType {
    TextOnly,
    Info,
    Matching;

    companion object {
        fun create(value: String): SnackBarType =
            entries.find { it.name == value } ?: TextOnly
    }
}

@Composable
fun PieceSnackBarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    snackbar: @Composable (SnackbarData) -> Unit = { Snackbar(it) }
) {
    val currentSnackbarData = hostState.currentSnackbarData
    val accessibilityManager = LocalAccessibilityManager.current
    LaunchedEffect(currentSnackbarData) {
        if (currentSnackbarData != null) {
            val duration =
                currentSnackbarData.visuals.duration.toMillis(
                    currentSnackbarData.visuals.actionLabel != null,
                    accessibilityManager
                )
            delay(duration)
            currentSnackbarData.dismiss()
        }
    }

    Crossfade(
        targetState = hostState.currentSnackbarData,
        modifier = modifier,
        label = "",
        content = { current -> if (current != null) snackbar(current) },
    )
}

private fun SnackbarDuration.toMillis(
    hasAction: Boolean,
    accessibilityManager: AccessibilityManager?
): Long {
    val original =
        when (this) {
            SnackbarDuration.Indefinite -> Long.MAX_VALUE
            SnackbarDuration.Long -> 10000L
            SnackbarDuration.Short -> 4000L
        }
    if (accessibilityManager == null) {
        return original
    }
    return accessibilityManager.calculateRecommendedTimeoutMillis(
        original,
        containsIcons = true,
        containsText = true,
        containsControls = hasAction
    )
}

@Preview
@Composable
private fun PieceSnackBarPreview() {
    PieceTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp),
        ) {
            PieceSnackBar(
                snackBarData = object : SnackbarData {
                    override val visuals = object : SnackbarVisuals {
                        override val actionLabel = null
                        override val duration = SnackbarDuration.Short
                        override val message = "텍스트만 있는 스낵바입니다"
                        override val withDismissAction = false
                    }

                    override fun dismiss() {}
                    override fun performAction() {}
                },
            )

            PieceSnackBar(
                snackBarData = object : SnackbarData {
                    override val visuals = object : SnackbarVisuals {
                        override val actionLabel = null
                        override val duration = SnackbarDuration.Short
                        override val message = "정보를 알려주는 스낵바입니다/Info"
                        override val withDismissAction = false
                    }

                    override fun dismiss() {}
                    override fun performAction() {}
                },
            )

            PieceSnackBar(
                snackBarData = object : SnackbarData {
                    override val visuals = object : SnackbarVisuals {
                        override val actionLabel = null
                        override val duration = SnackbarDuration.Short
                        override val message = "매칭과 관련된 스낵바입니다/Matching"
                        override val withDismissAction = false
                    }

                    override fun dismiss() {}
                    override fun performAction() {}
                },
            )
        }
    }
}
