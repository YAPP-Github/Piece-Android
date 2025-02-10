package com.puzzle.profile.graph.register.model

import com.puzzle.domain.model.profile.Answer

data class ValuePickRegisterRO(
    val id: Int,
    val category: String,
    val question: String,
    val answers: List<Answer>,
    val selectedAnswer: Int?,
)
