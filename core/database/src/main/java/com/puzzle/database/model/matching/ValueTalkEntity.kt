package com.puzzle.database.model.matching

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.puzzle.domain.model.profile.ValueTalkQuestion

@Entity(tableName = "value_talk")
data class ValueTalkEntity(
    @PrimaryKey val id: Int,
    val category: String,
    val title: String,
    val helpMessages: List<String>,
) {
    fun toDomain() = ValueTalkQuestion(
        id = id,
        category = category,
        title = title,
        guides = helpMessages,
    )
}
