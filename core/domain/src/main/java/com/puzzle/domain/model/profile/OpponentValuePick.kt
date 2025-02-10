package com.puzzle.domain.model.profile

data class OpponentValuePick(
    val category: String,
    val question: String,
    val answers: List<Answer>,
    val selectedAnswer: Int,
    val isSameWithMe: Boolean,
)
