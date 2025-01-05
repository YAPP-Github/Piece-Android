package com.puzzle.network.model.terms

import com.puzzle.common.parseDateTime
import com.puzzle.domain.model.terms.Term
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class LoadTermsResponse(
    val status: String?,
    val message: String?,
    val data: Data?,
) {
    @Serializable
    data class Data(
        val response: List<TermResponse>?,
    ) {
        fun toDomain() = response?.map { it.toDomain() } ?: emptyList()
    }
}

@Serializable
data class TermResponse(
    val termId: Int?,
    val title: String?,
    val content: String?,
    val required: Boolean?,
    val startDate: String?,
) {
    fun toDomain() = Term(
        termId = termId ?: -1,
        title = title ?: "UNKNOWN",
        content = content ?: "UNKNOWN",
        required = required ?: false,
        startDate = startDate?.parseDateTime() ?: LocalDateTime.MIN
    )
}
