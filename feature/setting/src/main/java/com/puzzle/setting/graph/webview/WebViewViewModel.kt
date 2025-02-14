package com.puzzle.setting.graph.webview

import androidx.lifecycle.ViewModel
import com.puzzle.navigation.NavigationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor(
    internal val navigationHelper: NavigationHelper,
) : ViewModel()
