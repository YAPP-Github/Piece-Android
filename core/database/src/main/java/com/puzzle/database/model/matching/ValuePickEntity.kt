package com.puzzle.database.model.matching

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.puzzle.domain.model.matching.Answer
import com.puzzle.domain.model.matching.ValuePick

data class ValuePickEntity(
    @Embedded val valuePickQuestion: ValuePickQuestion,
    @Relation(
        parentColumn = "id",
        entityColumn = "questionsId",
        entity = ValuePickAnswer::class,
    )
    val answers: List<ValuePickAnswer>,
) {
    fun toDomain() = ValuePick(
        id = valuePickQuestion.id,
        category = valuePickQuestion.category,
        question = valuePickQuestion.question,
        answers = answers.map {
            Answer(
                number = it.number,
                content = it.content,
            )
        },
        isSimilarToMe = false,
    )
}

@Entity(tableName = "value_pick_question")
data class ValuePickQuestion(
    @PrimaryKey val id: Int,
    val category: String,
    val question: String,
)

@Entity(
    tableName = "value_pick_answer",
    foreignKeys = [
        ForeignKey(
            entity = ValuePickQuestion::class,
            parentColumns = ["id"],
            childColumns = ["questionsId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("questionsId")]
)
data class ValuePickAnswer(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val questionsId: Int,
    val number: Int,
    val content: String,
)
