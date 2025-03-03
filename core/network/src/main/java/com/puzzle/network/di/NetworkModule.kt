package com.puzzle.network.di

import com.google.firebase.Firebase
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.firebase.remoteconfig.remoteConfig
import com.puzzle.network.BuildConfig.DEBUG
import com.puzzle.network.source.auth.AuthDataSource
import com.puzzle.network.source.auth.AuthDataSourceImpl
import com.puzzle.network.source.error.DebugErrorDataSourceImpl
import com.puzzle.network.source.error.ErrorDataSource
import com.puzzle.network.source.error.ErrorDataSourceImpl
import com.puzzle.network.source.matching.MatchingDataSource
import com.puzzle.network.source.matching.MatchingDataSourceImpl
import com.puzzle.network.source.profile.ProfileDataSource
import com.puzzle.network.source.profile.ProfileDataSourceImpl
import com.puzzle.network.source.term.TermDataSource
import com.puzzle.network.source.term.TermDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkBindsModule {

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

@Module
@InstallIn(SingletonComponent::class)
object NetworkProvidesModule {
    @Singleton
    @Provides
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig = Firebase.remoteConfig.apply {
        val configSettings = remoteConfigSettings { minimumFetchIntervalInSeconds = 3600 }
        setConfigSettingsAsync(configSettings)
    }

    @Provides
    @Singleton
    fun provideFirebaseCrashlytics(): FirebaseCrashlytics =
        FirebaseCrashlytics.getInstance()

    @Provides
    @Singleton
    @Debug
    fun provideDebugErrorDataSource(
        debugErrorDataSourceImpl: DebugErrorDataSourceImpl
    ): ErrorDataSource = debugErrorDataSourceImpl

    @Provides
    @Singleton
    @Release
    fun provideReleaseErrorDataSource(
        errorDataSourceImpl: ErrorDataSourceImpl
    ): ErrorDataSource = errorDataSourceImpl

    @Provides
    @Singleton
    fun provideErrorDataSource(
        @Debug debugErrorDataSource: ErrorDataSource,
        @Release releaseErrorDataSource: ErrorDataSource,
    ): ErrorDataSource {
        return if (DEBUG) debugErrorDataSource
        else releaseErrorDataSource
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Debug

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Release
