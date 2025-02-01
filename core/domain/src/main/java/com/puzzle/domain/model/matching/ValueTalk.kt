package com.puzzle.domain.model.matching

data class ValueTalk(
    val id: Int = 0,
    val category: String = "",
    val title: String = "",
    val answer: String = "",
    val summary: String = "",
    val helpMessages: List<String> = emptyList(),
)
