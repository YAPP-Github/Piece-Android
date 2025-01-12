package com.puzzle.domain.usecase

import com.puzzle.domain.model.auth.Timer
import com.puzzle.domain.repository.AuthRepository
import javax.inject.Inject

class VerifyAuthCodeUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        code: String,
        timer: Timer,
        callback: Callback,
    ) {
        if (!timer.isTimeRemaining()) return

        timer.pauseTimer()

        val result = authRepository.verifyAuthCode(code)

        if (result) {
            timer.stopTimer()

            callback.onVerificationCompleted()
        } else {
            timer.resumeTimer()

            callback.onVerificationFailed(
                IllegalArgumentException("Invalid code: $code")
            )
        }
    }

    interface Callback {
        fun onVerificationCompleted()
        fun onVerificationFailed(e: Throwable)
    }
}
