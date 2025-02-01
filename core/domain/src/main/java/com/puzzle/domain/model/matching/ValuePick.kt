package com.puzzle.domain.model.matching

data class ValuePick(
    val id: Int = 0,
    val category: String = "",
    val question: String = "",
    val option1: String = "",
    val option2: String = "",
    val isSimilarToMe: Boolean = true,
)
