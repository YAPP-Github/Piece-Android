package com.puzzle.network.di

import com.puzzle.network.source.auth.AuthDataSource
import com.puzzle.network.source.auth.AuthDataSourceImpl
import com.puzzle.network.source.matching.MatchingDataSource
import com.puzzle.network.source.matching.MatchingDataSourceImpl
import com.puzzle.network.source.profile.ProfileDataSource
import com.puzzle.network.source.profile.ProfileDataSourceImpl
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
    abstract fun bindsAuthDataSource(
        authDataSourceImpl: AuthDataSourceImpl,
    ): AuthDataSource

    @Binds
    @Singleton
    abstract fun bindsProfileDataSource(
        profileDataSourceImpl: ProfileDataSourceImpl,
    ): ProfileDataSource

    @Binds
    @Singleton
    abstract fun bindsTermDataSource(
        termDataSourceImpl: TermDataSourceImpl,
    ): TermDataSource

    @Binds
    @Singleton
    abstract fun bindsMatchingDataSource(
        matchingDataSourceImpl: MatchingDataSourceImpl,
    ): MatchingDataSource
}
