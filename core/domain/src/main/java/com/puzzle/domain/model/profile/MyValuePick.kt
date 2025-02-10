package com.puzzle.domain.model.profile

data class MyValuePick(
    val id: Int,
    val category: String,
    val question: String,
    val answers: List<Answer>,
    val selectedAnswer: Int,
)
