package com.puzzle.database.di

import com.puzzle.database.PieceDatabase
import com.puzzle.database.dao.TermDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {
    @Provides
    fun providesTopicsDao(
        database: PieceDatabase,
    ): TermDao = database.termDao()
}