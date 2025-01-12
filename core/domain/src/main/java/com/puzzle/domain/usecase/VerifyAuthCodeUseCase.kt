package com.puzzle.domain.usecase

import com.puzzle.domain.repository.AuthCodeRepository
import com.puzzle.domain.util.TimerManager
import javax.inject.Inject

class VerifyAuthCodeUseCase @Inject constructor(
    private val authCodeRepository: AuthCodeRepository,
) {
    suspend operator fun invoke(
        code: String,
        callback: Callback
    ) {
        if (!TimerManager.isTimeRemaining()) return

        val result = authCodeRepository.verify(code)

        if (result) {
            TimerManager.stopTimer()

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
