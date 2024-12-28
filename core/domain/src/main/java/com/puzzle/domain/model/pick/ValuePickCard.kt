package com.puzzle.domain.model.pick

data class ValuePickCard(
    val category: String = "",
    val question: String = "",
    val option1: String = "",
    val option2: String = "",
    val isSimilarToMe: Boolean = true,
)