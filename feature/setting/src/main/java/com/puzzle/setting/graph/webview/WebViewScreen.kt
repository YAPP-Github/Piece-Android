package com.puzzle.setting.graph.webview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.puzzle.designsystem.component.PieceSubBackTopBar
import com.puzzle.designsystem.component.PieceWebView
import com.puzzle.navigation.NavigationEvent

@Composable
internal fun WebViewRoute(
    title: String,
    url: String,
    viewModel: WebViewViewModel = hiltViewModel(),
) {
    WebViewScreen(
        title = title,
        url = url,
        onBackClick = { viewModel.navigationHelper.navigate(NavigationEvent.NavigateUp) },
    )
}

@Composable
private fun WebViewScreen(
    title: String,
    url: String,
    onBackClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        PieceSubBackTopBar(
            title = title,
            onBackClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        )

        PieceWebView(
            url = url,
            modifier = Modifier.weight(1f),
        )
    }
}
