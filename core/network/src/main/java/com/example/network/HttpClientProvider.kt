package com.example.network

import com.example.datastore.datastore.BloomPreferencesDataStore
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import timber.log.Timber

class HttpClientProvider(
    private val baseUrl: String,
    private val dataStore: BloomPreferencesDataStore,
) {
    fun create(): HttpClient =
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                        encodeDefaults = true
                    },
                )
            }

            install(Logging) {
                logger =
                    object : Logger {
                        override fun log(message: String) {
                            Timber.d("Ktor: $message")
                        }
                    }
                level = LogLevel.INFO
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val token = dataStore.token.first()
                        token?.let { BearerTokens(accessToken = it.first, refreshToken = null) }
                    }
                }
            }

            defaultRequest {
                url(baseUrl)
            }
        }
}
