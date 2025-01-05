package com.puzzle.database

import androidx.room.RoomDatabase

internal abstract class PieceDatabase : RoomDatabase() {

    companion object {
        internal const val NAME = "piece-database"
    }
}