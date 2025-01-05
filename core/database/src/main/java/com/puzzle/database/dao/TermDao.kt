package com.puzzle.database.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.puzzle.database.model.terms.TermEntity

interface TermDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTerms(vararg terms: TermEntity)

    @Query(value = "SELECT * FROM term")
    suspend fun getTerms(): List<TermEntity>

    @Query(value = "DELETE FROM term")
    suspend fun clearTerms()

    @Transaction
    suspend fun clearAndInsertTerms(vararg terms: TermEntity) {
        clearTerms()
        insertTerms(*terms)
    }
}
