package com.puzzle.designsystem.component

import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun PieceWebView(
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
