package com.puzzle.data.repository

import com.puzzle.domain.repository.AuthRepository
import com.puzzle.network.source.AuthDataSource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
) : AuthRepository {}
