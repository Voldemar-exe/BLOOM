package com.example.network.di

import com.example.bloom.core.network.BuildConfig
import com.example.datastore.datastore.BloomPreferencesDataStore
import com.example.network.HttpClientProvider
import com.example.network.api.AuthApi
import com.example.network.api.AuthApiImpl
import io.ktor.client.HttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule =
    module {
        single<String>(named("api.base.url")) { BuildConfig.API_URL }
        single<HttpClient> {
            HttpClientProvider(
                get<String>(named("api.base.url")),
                get<BloomPreferencesDataStore>(),
            ).create()
        }
        single<AuthApi> { AuthApiImpl(get<HttpClient>()) }
    }
