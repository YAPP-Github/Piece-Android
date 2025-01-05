package com.puzzle.network.model.terms

import com.puzzle.domain.model.terms.Term
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

data class LoadTermsResponse(
    val status: String?,
    val message: String?,
    val data: Data?,
) {
    data class Data(
        val response: List<TermResponse>?,
    ) {
        fun toDomain() = response?.map { it.toDomain() } ?: emptyList()
    }
}

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

// 현재는 여기에서만 사용되지만 3군데 이상에서 사용시 core:common 모듈을 만들고 그곳으로 이동
private fun String?.parseDateTime(): LocalDateTime {
    return try {
        this?.let { LocalDateTime.parse(it) } ?: LocalDateTime.MIN
    } catch (e: DateTimeParseException) {
        LocalDateTime.MIN
    }
}