package com.puzzle.domain.repository

interface AuthCodeRepository {
    suspend fun requestAuthCode(phoneNumber: String): Boolean
    suspend fun verify(code: String): Boolean
}