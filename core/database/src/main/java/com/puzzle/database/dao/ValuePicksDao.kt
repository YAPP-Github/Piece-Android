package com.puzzle.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.puzzle.database.model.matching.ValuePickEntity

@Dao
interface ValuePicksDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertValuePicks(vararg valuePicks: ValuePickEntity)

    @Transaction
    @Query("SELECT * FROM value_pick_question")
    suspend fun getValuePicks(): List<ValuePickEntity>

    @Transaction
    @Query("DELETE FROM value_pick_question")
    suspend fun clearValuePicks()

    @Transaction
    suspend fun replaceValuePicks(vararg valuePicks: ValuePickEntity) {
        clearValuePicks()
        insertValuePicks(*valuePicks)
    }
}
