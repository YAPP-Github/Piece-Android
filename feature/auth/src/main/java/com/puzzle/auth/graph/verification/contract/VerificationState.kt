package com.puzzle.auth.graph.verification.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.designsystem.R
import com.puzzle.domain.model.auth.Timer
import java.util.Locale

data class VerificationState(
    val isValidPhoneNumber: Boolean = true,
    val isAuthCodeRequested: Boolean = false,
    val authCodeStatus: AuthCodeStatus = AuthCodeStatus.INIT,
    val remainingTimeInSec: Long = Timer.DEFAULT_DURATION_IN_SEC,
) : MavericksState {

    val formattedRemainingTimeInSec: String = formatTime(remainingTimeInSec)

    private fun formatTime(seconds: Long): String {
        val minutes = seconds / 60
        val secs = seconds % 60
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, secs)
    }

    enum class AuthCodeStatus(val displayResId: Int) {
        INIT(displayResId = R.string.verification_do_not_share),
        VERIFIED(displayResId = R.string.verification_verified),
        INVALID(displayResId = R.string.verification_invalid_code),
        TIME_EXPIRED(displayResId = R.string.verification_time_expired),
    }
}