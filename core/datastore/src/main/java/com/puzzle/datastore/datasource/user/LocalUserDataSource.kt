package com.puzzle.datastore.datasource.user

import kotlinx.coroutines.flow.Flow

interface LocalUserDataSource {
    val userRole: Flow<String>
    suspend fun setUserRole(userRole: String)
    suspend fun clearUserRole()
}
