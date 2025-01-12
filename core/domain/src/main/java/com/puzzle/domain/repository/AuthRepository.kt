package com.puzzle.domain.repository

interface AuthRepository {
    suspend fun requestAuthCode(phoneNumber: String): Boolean
    suspend fun verifyAuthCode(code: String): Boolean
}
