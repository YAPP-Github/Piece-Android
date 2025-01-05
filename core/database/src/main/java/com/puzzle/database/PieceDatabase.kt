package com.puzzle.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.puzzle.database.dao.TermDao
import com.puzzle.database.model.terms.TermEntity

@Database(
    entities = [
        TermEntity::class,
    ],
    version = 1,
)
internal abstract class PieceDatabase : RoomDatabase() {
    abstract fun termDao(): TermDao

    companion object {
        internal const val NAME = "piece-database"
    }
}