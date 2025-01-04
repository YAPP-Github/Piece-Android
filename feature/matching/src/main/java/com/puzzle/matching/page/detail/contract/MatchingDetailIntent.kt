package com.puzzle.matching.page.detail.contract

sealed class MatchingDetailIntent {
    data object OnMatchingDetailCloseClick : MatchingDetailIntent()
    data object OnPreviousPageClick : MatchingDetailIntent()
    data object OnNextPageClick : MatchingDetailIntent()
    data object OnMoreClick : MatchingDetailIntent()
}