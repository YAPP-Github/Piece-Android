package com.puzzle.network.source.error

import android.util.Log
import javax.inject.Inject

class DebugErrorDataSourceImpl @Inject constructor() : ErrorDataSource {
    override suspend fun logError(exception: Throwable) {
        Log.e("DebugErrorDataSource", exception.stackTraceToString())
    }
}
