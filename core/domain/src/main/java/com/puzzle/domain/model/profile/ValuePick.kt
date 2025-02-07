package com.puzzle.domain.model.profile

data class ValuePick(
    val id: Int = 0,
    val category: String = "",
    val question: String = "",
    val selectedAnswer: Int = 0,
    val answers: List<Answer> = emptyList(),
    val isSimilarToMe: Boolean = true,
)

data class Answer(
    val number: Int,
    val content: String,
)

data class ValuePickAnswer(
    val valuePickId: Int,
    val selectedAnswer: Int,
)
