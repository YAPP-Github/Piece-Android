package com.puzzle.domain.model.profile

data class ValuePickQuestion(
    val id: Int,
    val category: String,
    val question: String,
    val answers: List<Answer>,
)

data class Answer(
    val number: Int,
    val content: String,
)

data class ValuePickAnswer(
    val valuePickId: Int,
    val selectedAnswer: Int?,
)
