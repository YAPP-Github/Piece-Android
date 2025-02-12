package com.puzzle.domain.repository

interface ErrorRepository {
    suspend fun logError(exception: Throwable)
}
