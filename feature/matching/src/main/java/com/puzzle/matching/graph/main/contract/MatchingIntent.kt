package com.puzzle.matching.graph.main.contract

sealed class MatchingIntent {
    data object OnButtonClick : MatchingIntent()
    data object OnCheckMyProfileClick : MatchingIntent()
    data object OnMatchingDetailClick : MatchingIntent()
    data object OnEditProfileClick : MatchingIntent()
}
