package com.puzzle.domain.repository

interface AuthRepository {
    suspend fun requestAuthCode(phoneNumber: String): Result<Boolean>
    suspend fun verifyAuthCode(code: String): Result<Boolean>
}
