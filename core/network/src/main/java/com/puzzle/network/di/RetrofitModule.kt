package com.puzzle.network.di

import com.launchdarkly.eventsource.ConnectStrategy
import com.launchdarkly.eventsource.EventSource
import com.launchdarkly.eventsource.background.BackgroundEventHandler
import com.launchdarkly.eventsource.background.BackgroundEventSource
import com.puzzle.network.BuildConfig
import com.puzzle.network.adapter.PieceCallAdapterFactory
import com.puzzle.network.api.PieceApi
import com.puzzle.network.authenticator.PieceAuthenticator
import com.puzzle.network.interceptor.PieceInterceptor
import com.puzzle.network.interceptor.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.net.URL
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        interceptor: PieceInterceptor,
        authenticator: PieceAuthenticator,
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .authenticator(authenticator)

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }

        return builder.build()
    }

    @Singleton
    @Provides
    fun providesPieceApi(
        json: Json,
        okHttpClient: OkHttpClient,
        callAdapterFactory: PieceCallAdapterFactory,
    ): PieceApi = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .addCallAdapterFactory(callAdapterFactory)
        .baseUrl(BuildConfig.PIECE_BASE_URL)
        .build()
        .create(PieceApi::class.java)

    @Singleton
    @Provides
    fun providesSseEventSource(
        sseEventHandler: BackgroundEventHandler,
        tokenManager: TokenManager,
    ): BackgroundEventSource = BackgroundEventSource.Builder(
        sseEventHandler,
        EventSource.Builder(
            ConnectStrategy
                .http(URL(BuildConfig.PIECE_BASE_URL))
                .header(
                    "Authorization",
                    "Bearer ${runBlocking { tokenManager.getAccessToken() }}"
                )
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(600, TimeUnit.SECONDS)
        )
    ).build()
}
