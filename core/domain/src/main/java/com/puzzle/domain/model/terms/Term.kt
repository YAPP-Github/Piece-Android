package com.puzzle.domain.model.terms

import java.time.LocalDateTime

data class Term(
    val termId: Int,
    val title: String,
    val content: String,
    val required: Boolean,
    val startDate: LocalDateTime,
)
