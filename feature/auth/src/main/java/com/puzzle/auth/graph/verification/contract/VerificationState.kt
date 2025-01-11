package com.puzzle.auth.graph.verification.contract

import com.airbnb.mvrx.MavericksState

data class VerificationState(
    val isValidPhoneNumber: Boolean = true,
    val hasStarted: Boolean = false,
    val remainingTimeInSec: Int = 0,
    val verificationCodeStatus: VerificationCodeStatus = VerificationCodeStatus.DO_NOT_SHARE,
    val isVerified: Boolean = false,
) : MavericksState {

    enum class VerificationCodeStatus {
        DO_NOT_SHARE, // "어떤 경우에도 타인에게 공유하지 마세요"
        VERIFIED, // "전화번호 인증을 완료했어요"
        INVALID, // "올바른 인증번호가 아니에요"
        TIME_EXPIRED, // "유효시간이 지났어요! ‘인증번호 재전송’을 눌러주세요"
    }
}