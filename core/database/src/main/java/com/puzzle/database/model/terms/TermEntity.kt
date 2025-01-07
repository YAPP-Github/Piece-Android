package com.puzzle.database.model.terms

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.puzzle.domain.model.terms.Term
import java.time.LocalDateTime

@Entity(tableName = "term")
data class TermEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    val title: String,
    val content: String,
    val required: Boolean,
    @ColumnInfo(name = "start_date") val startDate: LocalDateTime,
) {
    fun toDomain() = Term(
        termId = id,
        title = title,
        content = content,
        required = required,
        startDate = startDate,
    )
}
