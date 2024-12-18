package com.puzzle.auth

import androidx.lifecycle.ViewModel
import com.puzzle.navigation.NavigationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val navigationHelper: NavigationHelper,
) : ViewModel() {}