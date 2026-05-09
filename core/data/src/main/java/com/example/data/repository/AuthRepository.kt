package com.example.data.repository

import com.example.datastore.datastore.BloomPreferencesDataStore
import com.example.network.api.AuthApi
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val authToken: Flow<Pair<String, Boolean>?>

    suspend fun skipAuth()

    suspend fun login(
        login: String,
        password: String,
    ): Result<Unit>

    suspend fun register(
        login: String,
        email: String,
        password: String,
    ): Result<Unit>

    suspend fun logout()
}

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val dataStore: BloomPreferencesDataStore,
) : AuthRepository {
    override val authToken: Flow<Pair<String, Boolean>?> = dataStore.token

    override suspend fun skipAuth() {
        dataStore.saveToken(token = "", isSkipped = true)
    }

    override suspend fun login(
        login: String,
        password: String,
    ): Result<Unit> =
        authApi.login(login, password).map { response ->
            dataStore.saveToken(response.token)
        }

    override suspend fun register(
        login: String,
        email: String,
        password: String,
    ): Result<Unit> =
        authApi.register(login, email, password).map { response ->
            dataStore.saveToken(response.token)
        }

    override suspend fun logout() {
        dataStore.clearToken()
    }
}
