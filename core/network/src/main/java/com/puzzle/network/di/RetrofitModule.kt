package com.puzzle.network.di

import com.puzzle.domain.model.error.ErrorHelper
import com.puzzle.network.BuildConfig
import com.puzzle.network.adapter.PieceCallAdapterFactory
import com.puzzle.network.api.PieceApi
import com.puzzle.network.api.sse.SseClient
import com.puzzle.network.api.sse.SseEventHandler
import com.puzzle.network.authenticator.PieceAuthenticator
import com.puzzle.network.interceptor.PieceInterceptor
import com.puzzle.network.interceptor.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
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

    @Provides
    @Singleton
    fun providesSseEventHandler(
        json: Json,
        errorHelper: ErrorHelper,
    ): SseEventHandler = SseEventHandler(errorHelper, json)

    @Provides
    @Singleton
    fun providesSseClient(
        sseEventHandler: SseEventHandler,
        tokenManager: TokenManager,
    ): SseClient = SseClient(sseEventHandler, tokenManager)
}
