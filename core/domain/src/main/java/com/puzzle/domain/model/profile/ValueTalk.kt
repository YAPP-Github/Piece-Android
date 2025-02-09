package com.puzzle.domain.model.profile

data class ValueTalk(
    val id: Int = 0,
    val category: String = "",
    val title: String = "",
    val answer: String = "",
    val summary: String = "",
    val guides: List<String> = emptyList(),
)

data class ValueTalkAnswer(
    val valueTalkId: Int,
    val answer: String,
)
