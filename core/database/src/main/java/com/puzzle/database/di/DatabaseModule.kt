package com.puzzle.database.di

import android.content.Context
import androidx.room.Room
import com.puzzle.database.PieceDatabase
import com.puzzle.database.source.matching.LocalMatchingDataSource
import com.puzzle.database.source.matching.LocalMatchingDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseProvidesModule {
    @Provides
    @Singleton
    fun providesPieceDatabase(
        @ApplicationContext context: Context,
    ): PieceDatabase = Room.databaseBuilder(
        context,
        PieceDatabase::class.java,
        PieceDatabase.NAME,
    ).build()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseBindsModule {

    @Binds
    @Singleton
    abstract fun bindsLocalMatchingDataSource(
        localMatchingDataSourceImpl: LocalMatchingDataSourceImpl
    ): LocalMatchingDataSource
}
