package com.puzzle.database.converter

import androidx.room.TypeConverter
import com.puzzle.common.parseDateTime
import java.time.LocalDateTime

class PieceConverters {
    @TypeConverter
    fun fromLocalDate(date: LocalDateTime?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDateTime? {
        return dateString?.parseDateTime()
    }

    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.joinToString(",")
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return value?.split(",")?.filter { it.isNotEmpty() }
    }
}
