package com.puzzle.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.puzzle.database.dao.TermsDao
import com.puzzle.database.model.terms.TermEntity

@Database(
    entities = [
        TermEntity::class,
    ],
    version = 1,
)
internal abstract class PieceDatabase : RoomDatabase() {
    abstract fun termsDao(): TermsDao

    companion object {
        internal const val NAME = "piece-database"
    }
}