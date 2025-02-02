package com.puzzle.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.puzzle.database.converter.PieceConverters
import com.puzzle.database.dao.TermsDao
import com.puzzle.database.dao.ValuePicksDao
import com.puzzle.database.dao.ValueTalksDao
import com.puzzle.database.model.matching.ValuePickAnswer
import com.puzzle.database.model.matching.ValuePickQuestion
import com.puzzle.database.model.matching.ValueTalkEntity
import com.puzzle.database.model.terms.TermEntity

@Database(
    entities = [
        TermEntity::class,
        ValuePickQuestion::class,
        ValuePickAnswer::class,
        ValueTalkEntity::class,
    ],
    version = 1,
)
@TypeConverters(PieceConverters::class)
internal abstract class PieceDatabase : RoomDatabase() {
    abstract fun termsDao(): TermsDao
    abstract fun valuePicksDao(): ValuePicksDao
    abstract fun valueTalksDao(): ValueTalksDao

    companion object {
        internal const val NAME = "piece-database"
    }
}
