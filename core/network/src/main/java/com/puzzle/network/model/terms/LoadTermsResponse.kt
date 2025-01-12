package com.puzzle.network.model.terms

import com.puzzle.common.parseDateTime
import com.puzzle.domain.model.terms.Term
import com.puzzle.network.model.UNKNOWN_INT
import com.puzzle.network.model.UNKNOWN_STRING
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Suppress("PLUGIN_IS_NOT_ENABLED")
@Serializable
data class LoadTermsResponse(
    val responses: List<TermResponse>?,
) {
    fun toDomain() = responses?.map(TermResponse::toDomain) ?: emptyList()
}

@Serializable
data class TermResponse(
    val termId: Int?,
    val title: String?,
    val content: String?,
    val required: Boolean?,
    val startDate: String?,
) {
    fun toDomain(): Term = Term(
        id = termId ?: UNKNOWN_INT,
        title = title ?: UNKNOWN_STRING,
        content = content ?: UNKNOWN_STRING,
        required = required ?: false,
        startDate = startDate?.parseDateTime() ?: LocalDateTime.MIN
    )
}
