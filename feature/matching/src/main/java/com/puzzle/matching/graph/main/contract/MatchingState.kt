package com.puzzle.matching.graph.main.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.domain.model.match.MatchInfo
import com.puzzle.domain.model.user.UserRole

data class MatchingState(
    val isNotificationEnabled: Boolean = false,
    val userRole: UserRole? = null,
    val matchInfo: MatchInfo? = null,
    val isImageRejected: Boolean = false,
    val isDescriptionRejected: Boolean = false,
) : MavericksState
