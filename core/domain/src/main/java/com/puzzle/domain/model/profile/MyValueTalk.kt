package com.puzzle.domain.model.profile

data class MyValueTalk(
    val id: Int,
    val category: String,
    val title: String,
    val answer: String,
    val guides: List<String>,
    val summary: String,
)
