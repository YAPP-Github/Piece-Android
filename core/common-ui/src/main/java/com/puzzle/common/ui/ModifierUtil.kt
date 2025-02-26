package com.puzzle.common.ui

import androidx.compose.animation.core.Spring.StiffnessMediumLow
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.addFocusCleaner(
    focusManager: FocusManager,
    onFocusCleared: () -> Unit = {},
): Modifier {
    return this.pointerInput(Unit) {
        detectTapGestures(
            onTap = {
                onFocusCleared()
                focusManager.clearFocus()
            },
        )
    }
}

@Composable
fun Modifier.clickable(
    enabled: Boolean = true,
    isRipple: Boolean = false,
    onClick: () -> Unit,
): Modifier = composed {
    this.clickable(
        indication = if (isRipple) LocalIndication.current else null,
        interactionSource = remember { MutableInteractionSource() },
        enabled = enabled,
        onClick = onClick,
    )
}

@Composable
fun Modifier.throttledClickable(
    throttleTime: Long,
    enabled: Boolean = true,
    isRipple: Boolean = false,
    onClick: () -> Unit,
): Modifier = composed {
    var lastClickTime by remember { mutableLongStateOf(0L) }

    this.clickable(
        indication = if (isRipple) LocalIndication.current else null,
        interactionSource = remember { MutableInteractionSource() },
        enabled = enabled,
    ) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime >= throttleTime) {
            onClick()
            lastClickTime = currentTime
        }
    }
}

@Composable
fun Modifier.verticalScrollbar(
    state: LazyListState,
    width: Dp = 6.dp,
    color: Color,
): Modifier {
    val targetAlpha = if (state.isScrollInProgress) .7f else 0f
    val duration = if (state.isScrollInProgress) 150 else 1000

    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = duration)
    )

    val firstIndex by animateFloatAsState(
        targetValue = state.layoutInfo.visibleItemsInfo.firstOrNull()?.index?.toFloat() ?: 0f,
        animationSpec = spring(stiffness = StiffnessMediumLow)
    )

    val lastIndex by animateFloatAsState(
        targetValue = state.layoutInfo.visibleItemsInfo.lastOrNull()?.index?.toFloat() ?: 0f,
        animationSpec = spring(stiffness = StiffnessMediumLow)
    )

    return drawWithContent {
        drawContent()

        val itemsCount = state.layoutInfo.totalItemsCount

        if (itemsCount > 0 && alpha > 0f) {
            val scrollbarTop = firstIndex / itemsCount * size.height
            val scrollBottom = (lastIndex + 1f) / itemsCount * size.height
            val scrollbarHeight = scrollBottom - scrollbarTop
            drawRoundRect(
                color = color,
                cornerRadius = CornerRadius(0.1f),
                topLeft = Offset(size.width - width.toPx(), scrollbarTop),
                size = Size(width.toPx(), scrollbarHeight),
                alpha = alpha
            )
        }
    }
}

@Composable
fun Modifier.windowInsetsPadding(): Modifier = composed {
    this.padding(
        top = WindowInsets.systemBars
            .asPaddingValues()
            .calculateTopPadding(),
        bottom = WindowInsets.navigationBars
            .asPaddingValues()
            .calculateBottomPadding(),
    )
}
