package com.puzzle.domain.model.profile

data class ValueTalkQuestion(
    val id: Int,
    val category: String,
    val title: String,
    val placeholder: String,
    val guides: List<String>,
)

data class ValueTalkAnswer(
    val valueTalkId: Int,
    val answer: String,
)
