package com.puzzle.database.di

import android.content.Context
import androidx.room.Room
import com.puzzle.database.PieceDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
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