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
}
