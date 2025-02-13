package com.puzzle.network.source.error

interface ErrorDataSource {
    suspend fun logError(exception: Throwable)
}
