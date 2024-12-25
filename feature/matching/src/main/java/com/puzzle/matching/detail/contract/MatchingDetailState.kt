package com.puzzle.matching.detail.contract

import androidx.annotation.StringRes
import com.airbnb.mvrx.MavericksState
import com.puzzle.matching.R

data class MatchingDetailState(
    val isLoading: Boolean = false,
    val currentPage: MatchingDetailPage = MatchingDetailPage.BASIC_INFO,
) : MavericksState {

    enum class MatchingDetailPage(@StringRes val titleResId: Int) {
        BASIC_INFO(titleResId = R.string.feature_matching_detail_basicinfo_title),
        VALUE_TALK(titleResId = R.string.feature_matching_detail_valuetalk_title),
        VALUE_PICK(titleResId = R.string.feature_matching_detail_valuepick_title)
    }
}