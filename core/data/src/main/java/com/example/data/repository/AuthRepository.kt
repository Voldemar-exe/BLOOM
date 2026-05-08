package com.example.data.repository

import com.example.datastore.datastore.BloomPreferencesDataStore
import com.example.network.api.AuthApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface AuthRepository {
    fun isAuthorized(): Flow<Boolean>

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
    override fun isAuthorized(): Flow<Boolean> = dataStore.token.map { it?.isNotEmpty() ?: false }

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
