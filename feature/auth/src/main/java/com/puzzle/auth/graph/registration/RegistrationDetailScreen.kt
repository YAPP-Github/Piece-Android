package com.puzzle.auth.graph.registration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.puzzle.designsystem.component.PieceSubBackTopBar

@Composable
internal fun RegistrationDetailScreen(
    termTitle: String,
    onBackClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        PieceSubBackTopBar(
            title = termTitle,
            onBackClick = onBackClick,
            modifier = Modifier
        )
    }
}