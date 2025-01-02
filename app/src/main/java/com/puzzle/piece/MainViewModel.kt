package com.puzzle.piece

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.puzzle.domain.model.error.ErrorHelper
import com.puzzle.domain.model.error.HttpResponseException
import com.puzzle.navigation.NavigationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    internal val navigationHelper: NavigationHelper,
    private val errorHelper: ErrorHelper,
) : ViewModel() {

    init {
        handleError()
    }

    private fun handleError() = viewModelScope.launch {
        errorHelper.errorEvent.collect { exception ->
            when (exception) {
                is HttpResponseException -> {
                    // Todo : HTTP 호출 에러
                    return@collect
                }

                // Todo : 그 외 IoException 등등..
            }
        }
    }
}
