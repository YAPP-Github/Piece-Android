package com.puzzle.domain.usecase

import com.puzzle.domain.repository.AuthCodeRepository
import com.puzzle.domain.util.TimerManager
import javax.inject.Inject

class RequestAuthCodeUseCase @Inject constructor(
    private val authCodeRepository: AuthCodeRepository,
) {
    suspend operator fun invoke(
        phoneNumber: String,
        callback: Callback
    ) {
        val result = authCodeRepository.requestAuthCode(phoneNumber)

        if (!result) {
            callback.onRequestFail(
                IllegalStateException("Failed to request auth code for $phoneNumber")
            )
            return
        }

        callback.onRequestSuccess()

        TimerManager.startTimer(
            durationInSec = DURATION_IN_SEC,
            onTick = { remaining ->
                callback.onTick(remaining)
            },
            onTimeExpired = {
                callback.onTimeExpired()
            }
        )
    }

    private companion object {
        const val DURATION_IN_SEC = 3
    }

    interface Callback {
        fun onRequestSuccess()
        fun onRequestFail(e: Throwable)
        fun onTimeExpired()
        fun onTick(remainingTimeInSec: Int)
    }
}
