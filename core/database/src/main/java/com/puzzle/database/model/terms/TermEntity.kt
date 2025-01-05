package com.puzzle.database.model.terms

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.puzzle.common.parseDateTime
import com.puzzle.domain.model.terms.Term
import java.time.LocalDateTime

@Entity(tableName = "term")
data class TermEntity(
    @PrimaryKey
    @ColumnInfo(name = "term_id") val termId: Int?,
    val title: String?,
    val content: String?,
    val required: Boolean?,
    @ColumnInfo(name = "start_date") val startDate: String?,
) {
    fun toDomain() = Term(
        termId = termId ?: -1,
        title = title ?: "UNKNOWN",
        content = content ?: "UNKNOWN",
        required = required ?: false,
        startDate = startDate?.parseDateTime() ?: LocalDateTime.MIN
    )
}
