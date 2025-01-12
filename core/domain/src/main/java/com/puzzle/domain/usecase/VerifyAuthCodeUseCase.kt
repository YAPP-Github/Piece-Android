package com.puzzle.domain.usecase

import com.puzzle.domain.repository.AuthCodeRepository
import com.puzzle.domain.util.TimerManager
import javax.inject.Inject

class VerifyAuthCodeUseCase @Inject constructor(
    private val authCodeRepository: AuthCodeRepository,
    private val timerManager: TimerManager,
) {
    suspend operator fun invoke(
        code: String,
        callback: Callback
    ) {
        if (!timerManager.isTimeRemaining()) return

        val result = authCodeRepository.verify(code)

        if (result) {
            timerManager.stopTimer()

            callback.onVerificationCompleted()
        } else {
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
