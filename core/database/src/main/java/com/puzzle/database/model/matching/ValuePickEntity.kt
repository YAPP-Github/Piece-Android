package com.puzzle.database.model.matching

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.puzzle.domain.model.profile.AnswerOption
import com.puzzle.domain.model.profile.ValuePickQuestion

data class ValuePickEntity(
    @Embedded val valuePickQuestion: ValuePickQuestionEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "questionsId",
        entity = ValuePickAnswerEntity::class,
    )
    val answers: List<ValuePickAnswerEntity>,
) {
    fun toDomain() = ValuePickQuestion(
        id = valuePickQuestion.id,
        category = valuePickQuestion.category,
        question = valuePickQuestion.question,
        answerOptions = answers.map {
            AnswerOption(
                number = it.number,
                content = it.content,
            )
        },
    )
}

@Entity(tableName = "value_pick_question")
data class ValuePickQuestionEntity(
    @PrimaryKey val id: Int,
    val category: String,
    val question: String,
)

@Entity(
    tableName = "value_pick_answer",
    foreignKeys = [
        ForeignKey(
            entity = ValuePickQuestionEntity::class,
            parentColumns = ["id"],
            childColumns = ["questionsId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("questionsId")]
)
data class ValuePickAnswerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val questionsId: Int,
    val number: Int,
    val content: String,
)
