package com.puzzle.data.repository

import com.puzzle.domain.repository.VerificationCodeRepository

class VerificationCodeRepositoryImpl : VerificationCodeRepository {
    override suspend fun requestVerificationCode(phoneNumber: String) {

    }

    override suspend fun verify(code: String): Boolean {
        return true
    }
}