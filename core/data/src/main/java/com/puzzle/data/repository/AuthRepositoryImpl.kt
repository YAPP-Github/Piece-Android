package com.puzzle.data.repository

import com.puzzle.domain.repository.AuthRepository
import com.puzzle.network.source.AuthDataSource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
) : AuthRepository {

    override suspend fun requestAuthCode(phoneNumber: String): Boolean {
        return true
    }

    override suspend fun verifyAuthCode(code: String): Boolean {
        return true
    }
}
