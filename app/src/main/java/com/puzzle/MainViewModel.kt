package com.puzzle

import androidx.lifecycle.ViewModel
import com.puzzle.navigation.NavigationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    internal val navigationHelper: NavigationHelper,
) : ViewModel() {
    internal val navigationFlow = navigationHelper.navigationFlow
}