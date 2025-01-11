package com.puzzle.domain.repository

interface VerificationCodeRepository {
    suspend fun requestVerificationCode(phoneNumber: String): Boolean
    suspend fun verify(code: String): Boolean
}