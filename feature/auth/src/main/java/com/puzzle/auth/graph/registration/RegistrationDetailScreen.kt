package com.puzzle.auth.graph.registration

import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.puzzle.designsystem.component.PieceSolidButton
import com.puzzle.designsystem.component.PieceSubBackTopBar
import com.puzzle.domain.model.terms.Term

@Composable
internal fun RegistrationDetailScreen(
    term: Term,
    onBackClick: () -> Unit,
    onAgreeClick: () -> Unit,
) {
    BackHandler { onBackClick() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
    ) {
        PieceSubBackTopBar(
            title = term.title,
            onBackClick = onBackClick,
        )

        PieceWebView(
            url = term.content,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        )

        PieceSolidButton(
            label = "동의하기",
            onClick = onAgreeClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 10.dp),
        )
    }
}

@Composable
internal fun PieceWebView(
    url: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var webView by remember { mutableStateOf<WebView?>(null) }

    AndroidView(
        factory = {
            webView = WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = object : WebViewClient() {}
                webChromeClient = object : WebChromeClient() {}
            }
            webView!!
        },
        update = { it.loadUrl(url) },
        onRelease = { webView?.destroy() },
        modifier = modifier,
    )
}
