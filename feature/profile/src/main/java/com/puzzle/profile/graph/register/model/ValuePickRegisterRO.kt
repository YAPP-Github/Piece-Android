package com.puzzle.profile.graph.register.model

import com.puzzle.domain.model.profile.AnswerOption

data class ValuePickRegisterRO(
    val id: Int,
    val category: String,
    val question: String,
    val answerOptions: List<AnswerOption>,
    val selectedAnswer: Int?,
)
