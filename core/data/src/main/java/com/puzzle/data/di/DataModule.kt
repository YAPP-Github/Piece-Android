package com.puzzle.data.di

import com.puzzle.data.repository.AuthRepositoryImpl
import com.puzzle.data.repository.TermsRepositoryImpl
import com.puzzle.data.repository.AuthCodeRepositoryImpl
import com.puzzle.domain.repository.AuthRepository
import com.puzzle.domain.repository.TermsRepository
import com.puzzle.domain.repository.AuthCodeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindsAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl,
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindsTermsRepository(
        termsRepositoryImpl: TermsRepositoryImpl,
    ): TermsRepository

    @Binds
    @Singleton
    abstract fun bindsAuthCodeRepository(
        authCodeRepositoryImpl: AuthCodeRepositoryImpl,
    ): AuthCodeRepository
}
