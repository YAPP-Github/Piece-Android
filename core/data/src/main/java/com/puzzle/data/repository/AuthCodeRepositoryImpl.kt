package com.puzzle.data.repository

import com.puzzle.domain.repository.AuthCodeRepository
import javax.inject.Inject

class AuthCodeRepositoryImpl @Inject constructor() : AuthCodeRepository {
    override suspend fun requestAuthCode(phoneNumber: String): Boolean {
        return true
    }

    override suspend fun verify(code: String): Boolean {
        return true
    }
}