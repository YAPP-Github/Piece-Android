package com.puzzle.database.di

import com.puzzle.database.PieceDatabase
import com.puzzle.database.dao.TermsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {
    @Provides
    fun providesTermsDao(
        database: PieceDatabase,
    ): TermsDao = database.termsDao()
}
