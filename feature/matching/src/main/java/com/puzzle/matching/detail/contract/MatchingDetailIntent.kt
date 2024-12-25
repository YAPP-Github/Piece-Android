package com.puzzle.matching.detail.contract

sealed class MatchingDetailIntent {
    data object OnMatchingDetailCloseClick: MatchingDetailIntent()
}