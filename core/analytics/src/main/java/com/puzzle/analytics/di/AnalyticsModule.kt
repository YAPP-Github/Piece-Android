package com.puzzle.analytics.di

import android.content.Context
import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration
import com.puzzle.analytics.AmplitudeAnalyticsHelper
import com.puzzle.analytics.AnalyticsHelper
import com.puzzle.analytics.BuildConfig
import com.puzzle.analytics.DebugAnalyticsHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {

    @Provides
    @Singleton
    fun providesAmplitude(@ApplicationContext context: Context): Amplitude = Amplitude(
        Configuration(
            apiKey = BuildConfig.AMPLITUDE_API_KEY,
            context = context,
        )
    )

    @Provides
    @Singleton
    @Debug
    fun provideDebugAnalyticsHelper(): AnalyticsHelper = DebugAnalyticsHelper()

    @Provides
    @Singleton
    @Release
    fun provideReleaseAnalyticsHelper(amplitude: Amplitude): AnalyticsHelper =
        AmplitudeAnalyticsHelper(amplitude)

    @Provides
    @Singleton
    fun provideAnalyticsHelper(
        @Debug debugHelper: AnalyticsHelper,
        @Release releaseHelper: AnalyticsHelper
    ): AnalyticsHelper = if (BuildConfig.DEBUG) debugHelper else releaseHelper
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Debug

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Release
