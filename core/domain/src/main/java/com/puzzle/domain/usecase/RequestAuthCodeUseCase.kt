package com.puzzle.domain.usecase

import com.puzzle.domain.model.auth.Timer
import com.puzzle.domain.repository.AuthRepository
import javax.inject.Inject

class RequestAuthCodeUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        phoneNumber: String,
        timer: Timer,
        callback: Callback,
    ) {
        val result = authRepository.requestAuthCode(phoneNumber)

        if (!result) {
            callback.onRequestFail(
                IllegalStateException("Failed to request auth code for $phoneNumber")
            )
            return
        }

        callback.onRequestSuccess()

        timer.startTimer(
            onTick = { remaining ->
                callback.onTick(remaining)
            },
            onTimeExpired = {
                callback.onTimeExpired()
            }
        )
    }

    interface Callback {
        fun onRequestSuccess()
        fun onRequestFail(e: Throwable)
        fun onTimeExpired()
        fun onTick(remainingTimeInSec: Int)
    }
}
