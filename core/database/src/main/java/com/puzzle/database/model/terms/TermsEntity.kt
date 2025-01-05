package com.puzzle.database.model.terms

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.puzzle.domain.model.terms.Term
import java.time.LocalDateTime

@Entity(
    tableName = TERMS_TABLE_NAME,
)
data class TermsEntity(
    @PrimaryKey val termId: Int?,
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

private const val TERMS_TABLE_NAME = "terms"