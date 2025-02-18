package com.puzzle.matching.graph.detail.contract

import androidx.compose.runtime.Composable

sealed class MatchingDetailIntent {
    data object OnMatchingDetailCloseClick : MatchingDetailIntent()
    data object OnPreviousPageClick : MatchingDetailIntent()
    data object OnNextPageClick : MatchingDetailIntent()
    data class OnReportClick(val matchId: Int) : MatchingDetailIntent()
    data class OnBlockClick(val matchId: Int) : MatchingDetailIntent()
    data object OnAcceptClick : MatchingDetailIntent()
    data object OnDeclineClick : MatchingDetailIntent()
    data class OnMoreClick(val content: @Composable () -> Unit) : MatchingDetailIntent()
}
