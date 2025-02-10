package com.puzzle.profile.graph.valuetalk.model

data class MyValueTalkRO(
    val id: Int,
    val category: String,
    val title: String,
    val answer: String,
    val summary: String,
    val guides: List<String>,
)
