package com.puzzle.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.puzzle.database.model.matching.ValueTalkEntity

@Dao
interface ValueTalksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertValueTalks(vararg valueTalks: ValueTalkEntity)

    @Query(value = "SELECT * FROM value_talk")
    suspend fun getValueTalks(): List<ValueTalkEntity>

    @Query(value = "DELETE FROM value_talk")
    suspend fun clearValueTalks()

    @Transaction
    suspend fun replaceValueTalks(vararg valueTalks: ValueTalkEntity) {
        clearValueTalks()
        insertValueTalks(*valueTalks)
    }
}
