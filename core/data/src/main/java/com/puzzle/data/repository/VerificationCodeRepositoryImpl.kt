package com.puzzle.data.repository

import com.puzzle.domain.repository.VerificationCodeRepository
import javax.inject.Inject

class VerificationCodeRepositoryImpl @Inject constructor() : VerificationCodeRepository {
    override suspend fun requestVerificationCode(phoneNumber: String): Boolean {
        return true
    }

    override suspend fun verify(code: String): Boolean {
        return true
    }
}