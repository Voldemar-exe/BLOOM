package com.example.network.di

import com.example.bloom.core.network.BuildConfig
import com.example.datastore.datastore.BloomPreferencesDataStore
import com.example.network.HttpClientProvider
import com.example.network.api.AuthApi
import com.example.network.api.AuthApiImpl
import com.example.network.api.SocialApi
import com.example.network.api.SocialApiImpl
import com.example.network.api.SyncApi
import com.example.network.api.SyncApiImpl
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule =
    module {
        single<String>(named("api.base.url")) { BuildConfig.API_URL }
        single<Json> {
            Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = true
            }
        }
        single<HttpClient> {
            HttpClientProvider(
                get<String>(named("api.base.url")),
                get<BloomPreferencesDataStore>(),
            ).create()
        }
        single<AuthApi> { AuthApiImpl(get<HttpClient>()) }
        single<SyncApi> { SyncApiImpl(get<HttpClient>()) }
        single<SocialApi> { SocialApiImpl(get<HttpClient>()) }
    }
