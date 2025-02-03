package com.puzzle.network.di

import com.puzzle.network.source.matching.MatchingDataSource
import com.puzzle.network.source.matching.MatchingDataSourceImpl
import com.puzzle.network.source.term.TermDataSource
import com.puzzle.network.source.term.TermDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    @Singleton
    abstract fun bindsMatchingDataSource(
        matchingDataSourceImpl: MatchingDataSourceImpl,
    ): MatchingDataSource

    @Binds
    @Singleton
    abstract fun bindsTermDataSource(
        termDataSourceImpl: TermDataSourceImpl,
    ): TermDataSource
}
