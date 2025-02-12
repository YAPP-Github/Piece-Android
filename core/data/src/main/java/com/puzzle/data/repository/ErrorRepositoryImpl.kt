package com.puzzle.data.repository

import com.puzzle.domain.repository.ErrorRepository
import com.puzzle.network.source.error.ErrorDataSource
import javax.inject.Inject

class ErrorRepositoryImpl @Inject constructor(
    private val errorDataSource: ErrorDataSource,
) : ErrorRepository {
    override suspend fun logError(exception: Throwable) = errorDataSource.logError(exception)
}
