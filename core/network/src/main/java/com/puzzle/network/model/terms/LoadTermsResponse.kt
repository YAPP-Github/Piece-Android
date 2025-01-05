package com.puzzle.network.model.terms

import android.util.Log
import com.puzzle.common.parseDateTime
import com.puzzle.domain.model.terms.Term
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class LoadTermsResponse(
    val responses: List<TermResponse>?,
) {
    fun toDomain() = responses?.map { it.toDomain() } ?: emptyList()
}

@Serializable
data class TermResponse(
    val termId: Int?,
    val title: String?,
    val content: String?,
    val required: Boolean?,
    val startDate: String?,
) {
    fun toDomain(): Term {
        Log.d("test", this.toString())

        return Term(
            termId = termId ?: -1,
            title = title ?: "UNKNOWN",
            content = content ?: "UNKNOWN",
            required = required ?: false,
            startDate = startDate?.parseDateTime() ?: LocalDateTime.MIN
        )
    }
}
