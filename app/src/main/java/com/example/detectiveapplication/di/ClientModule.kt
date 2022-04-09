package com.example.detectiveapplication.di

import com.example.detectiveapplication.utils.Constants.Companion.TIME_OUT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ClientModule {

    companion object {

        @Provides
        @Singleton
        fun provideClient(): HttpClient =
            HttpClient(Android) {
                install(JsonFeature) {
                    serializer = KotlinxSerializer(
                        kotlinx.serialization.json.Json {
                            isLenient = true
//                            ignoreUnknownKeys = true
                        }
                    )
                }

                install(HttpTimeout) {
                    requestTimeoutMillis = TIME_OUT
                }

            }
    }
}