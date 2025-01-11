package com.puzzle.domain.repository

interface VerificationCodeRepository {
    suspend fun requestVerificationCode(phoneNumber: String)
    suspend fun verify(code: String): Boolean
}