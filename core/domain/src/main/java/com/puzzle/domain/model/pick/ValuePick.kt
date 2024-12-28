package com.puzzle.domain.model.pick

data class ValuePick(
    val category: String = "",
    val question: String = "",
    val option1: String = "",
    val option2: String = "",
    val isSimilarToMe: Boolean = true,
)