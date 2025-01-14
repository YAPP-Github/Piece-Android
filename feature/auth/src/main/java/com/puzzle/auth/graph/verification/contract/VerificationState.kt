package com.puzzle.auth.graph.verification.contract

import com.airbnb.mvrx.MavericksState
import com.puzzle.designsystem.R

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
        return String.format("%02d:%02d", minutes, secs)
    }

    enum class AuthCodeStatus(val displayResId: Int) {
        INIT(displayResId = R.string.verification_do_not_share), // "어떤 경우에도 타인에게 공유하지 마세요"
        VERIFIED(displayResId = R.string.verification_verified), // "전화번호 인증을 완료했어요"
        INVALID(displayResId = R.string.verification_invalid_code), // "올바른 인증번호가 아니에요"
        TIME_EXPIRED(displayResId = R.string.verification_time_expired), // "유효시간이 지났어요! ‘인증번호 재전송’을 눌러주세요"
    }
}