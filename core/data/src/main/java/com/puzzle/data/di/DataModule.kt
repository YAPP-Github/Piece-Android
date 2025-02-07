package com.puzzle.data.di

import com.puzzle.data.TokenManagerImpl
import com.puzzle.data.image.ImageResizer
import com.puzzle.data.image.ImageResizerImpl
import com.puzzle.data.repository.AuthRepositoryImpl
import com.puzzle.data.repository.ProfileRepositoryImpl
import com.puzzle.data.repository.TermsRepositoryImpl
import com.puzzle.data.repository.UserRepositoryImpl
import com.puzzle.domain.repository.AuthRepository
import com.puzzle.domain.repository.ProfileRepository
import com.puzzle.domain.repository.TermsRepository
import com.puzzle.domain.repository.UserRepository
import com.puzzle.network.interceptor.TokenManager
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
    abstract fun bindsUserRepository(
        userRepositoryImpl: UserRepositoryImpl,
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindsProfileRepository(
        profileRepositoryImpl: ProfileRepositoryImpl,
    ): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindsTokenManager(
        tokenManagerImpl: TokenManagerImpl,
    ): TokenManager

    @Binds
    @Singleton
    abstract fun bindsImageResizer(
        imageResizerImpl: ImageResizerImpl,
    ): ImageResizer
}
