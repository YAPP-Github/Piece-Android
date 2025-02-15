package com.puzzle.network.di

import com.launchdarkly.eventsource.background.BackgroundEventHandler
import com.puzzle.domain.model.error.ErrorHelper
import com.puzzle.network.BuildConfig
import com.puzzle.network.adapter.PieceCallAdapterFactory
import com.puzzle.network.api.PieceApi
import com.puzzle.network.authenticator.PieceAuthenticator
import com.puzzle.network.interceptor.PieceInterceptor
import com.puzzle.network.interceptor.TokenManager
import com.puzzle.network.sse.SseClient
import com.puzzle.network.sse.SseEventHandler
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
        errorHelper: ErrorHelper,
    ): BackgroundEventHandler = SseEventHandler(errorHelper)

    @Provides
    @Singleton
    fun providesSseClient(
        sseEventHandler: BackgroundEventHandler,
        tokenManager: TokenManager,
        pieceApi: PieceApi,
    ): SseClient = SseClient(sseEventHandler, tokenManager, pieceApi)
}
