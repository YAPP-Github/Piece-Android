package com.puzzle.data.fake.source.user

import com.puzzle.datastore.datasource.user.LocalUserDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeLocalUserDataSource : LocalUserDataSource {
    private var currentUserRole = ""

    override val userRole: Flow<String> get() = flowOf(currentUserRole)

    override suspend fun setUserRole(userRole: String) {
        currentUserRole = userRole
    }

    override suspend fun clearUserRole() {
        currentUserRole = ""
    }
}
