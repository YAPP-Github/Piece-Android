package com.puzzle.matching.graph.main.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.domain.model.match.MatchInfo
import com.puzzle.domain.model.user.UserRole
import java.util.Locale

data class MatchingState(
    val userRole: UserRole? = null,
    val matchInfo: MatchInfo? = null,
    val isImageRejected: Boolean = false,
    val isDescriptionRejected: Boolean = false,
    val remainWaitingTimeInSec: Int = 0,
) : MavericksState {

    val formattedRemainWaitingTime: String = formatSecondsToTimeString(remainWaitingTimeInSec)

    val formattedRemainMatchingStartTime: String = matchInfo?.let {
        formatSecondsToTimeString(it.remainMatchingStartTimeInSec)
    } ?: ""

    private fun formatSecondsToTimeString(totalSeconds: Int): String {
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return String.format(Locale.KOREA, " %02d:%02d:%02d ", hours, minutes, seconds)
    }
}