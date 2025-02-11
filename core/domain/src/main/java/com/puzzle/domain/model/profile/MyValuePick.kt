package com.puzzle.domain.model.profile

data class MyValuePick(
    val id: Int,
    val category: String,
    val question: String,
    val answerOptions: List<AnswerOption>,
    val selectedAnswer: Int,
)
