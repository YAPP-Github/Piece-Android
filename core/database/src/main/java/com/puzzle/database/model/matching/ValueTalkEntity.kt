package com.puzzle.database.model.matching

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.puzzle.domain.model.matching.ValueTalk

@Entity(tableName = "value_talk")
data class ValueTalkEntity(
    @PrimaryKey val id: Int,
    val category: String,
    val title: String,
    val helpMessages: List<String>,
) {
    fun toDomain() = ValueTalk(
        id = id,
        category = category,
        title = title,
        helpMessages = helpMessages,
    )
}
