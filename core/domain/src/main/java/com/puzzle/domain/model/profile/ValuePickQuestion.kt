package com.puzzle.domain.model.profile

data class ValuePickQuestion(
    val id: Int,
    val category: String,
    val question: String,
    val answerOptions: List<AnswerOption>,
)

data class AnswerOption(
    val number: Int,
    val content: String,
)

data class ValuePickAnswer(
    val valuePickId: Int,
    val selectedAnswer: Int?,
)
