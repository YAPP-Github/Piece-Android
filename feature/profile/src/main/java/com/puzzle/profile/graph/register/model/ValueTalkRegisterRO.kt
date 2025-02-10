package com.puzzle.profile.graph.register.model

data class ValueTalkRegisterRO(
    val id: Int,
    val category: String,
    val title: String,
    val answer: String,
    val guides: List<String>,
)
