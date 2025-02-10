package com.puzzle.domain.model.profile

data class OpponentValuePick(
    val category: String,
    val question: String,
    val answerOptions: List<AnswerOption>,
    val selectedAnswer: Int,
    val isSameWithMe: Boolean,
)
