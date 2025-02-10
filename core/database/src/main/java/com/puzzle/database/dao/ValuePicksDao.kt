package com.puzzle.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.puzzle.database.model.matching.ValuePickAnswerEntity
import com.puzzle.database.model.matching.ValuePickEntity
import com.puzzle.database.model.matching.ValuePickQuestionEntity

@Dao
interface ValuePicksDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertValuePickQuestion(question: ValuePickQuestionEntity)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertValuePickAnswers(answers: List<ValuePickAnswerEntity>)

    @Transaction
    @Query("SELECT * FROM value_pick_question")
    suspend fun getValuePicks(): List<ValuePickEntity>

    @Transaction
    @Query("DELETE FROM value_pick_question")
    suspend fun clearValuePicks()

    @Transaction
    suspend fun replaceValuePicks(valuePick: ValuePickEntity) {
        clearValuePicks()
        insertValuePickQuestion(valuePick.valuePickQuestion)
        insertValuePickAnswers(valuePick.answers)
    }
}
