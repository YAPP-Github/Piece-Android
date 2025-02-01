package com.puzzle.domain.model.matching

data class ValueTalk(
    val label: String = "",
    val title: String = "",
    val content: String = "",
    val aiSummary: String = "",
    val helpMessages: List<String> = emptyList(),
)