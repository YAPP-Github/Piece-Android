package com.puzzle.designsystem.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults.ProgressAnimationSpec
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.puzzle.designsystem.foundation.PieceTheme

@Composable
fun PiecePageIndicator(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier,
) {
    val progress by animateFloatAsState(
        targetValue = (currentStep / totalSteps.toFloat()),
        animationSpec = ProgressAnimationSpec,
    )

    LinearProgressIndicator(
        progress = { progress },
        color = PieceTheme.colors.primaryDefault,
        trackColor = PieceTheme.colors.white,
        gapSize = 0.dp,
        drawStopIndicator = {},
        modifier = modifier
            .height(4.dp)
            .fillMaxWidth()
            .background(PieceTheme.colors.white),
    )
}

@Preview
@Composable
private fun PreviewPiecePageIndicator() {
    PieceTheme {
        PiecePageIndicator(
            currentStep = 3,
            totalSteps = 5,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
