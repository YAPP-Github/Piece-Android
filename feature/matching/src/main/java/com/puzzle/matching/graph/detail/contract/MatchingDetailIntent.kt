package com.puzzle.matching.graph.detail.contract

import androidx.compose.runtime.Composable

sealed class MatchingDetailIntent {
    data object OnMatchingDetailCloseClick : MatchingDetailIntent()
    data object OnPreviousPageClick : MatchingDetailIntent()
    data object OnNextPageClick : MatchingDetailIntent()
    data object OnReportClick : MatchingDetailIntent()
    data object OnBlockClick : MatchingDetailIntent()
    data class OnMoreClick(val content: @Composable () -> Unit) : MatchingDetailIntent()
}
