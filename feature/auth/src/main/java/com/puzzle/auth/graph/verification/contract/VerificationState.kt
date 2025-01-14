package com.puzzle.auth.graph.verification.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.designsystem.R
import java.util.Locale

data class VerificationState(
    val isValidPhoneNumber: Boolean = true,
    val isAuthCodeRequested: Boolean = false,
    val authCodeStatus: AuthCodeStatus = AuthCodeStatus.INIT,
    val isVerified: Boolean = false,
    private val _remainingTimeInSec: Int = 0,
) : MavericksState {

    val remainingTimeInSec: String = formatTime(_remainingTimeInSec)

    private fun formatTime(seconds: Int): String {
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