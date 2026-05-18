package com.example.network.api

import com.example.network.model.LoginRequest
import com.example.network.model.LoginResponse
import com.example.network.model.RegisterRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

interface AuthApi {
    suspend fun login(
        login: String,
        password: String,
    ): Result<LoginResponse>

    suspend fun register(
        login: String,
        email: String,
        password: String,
    ): Result<LoginResponse>

    suspend fun deleteAccount(): Result<Boolean>
}

class AuthApiImpl(private val httpClient: HttpClient) : AuthApi {
    override suspend fun login(
        login: String,
        password: String,
    ): Result<LoginResponse> =
        runCatching {
            httpClient
                .post("/login") {
                    contentType(ContentType.Application.Json)
                    setBody(LoginRequest(login, password))
                }.body<LoginResponse>()
        }

    override suspend fun register(
        login: String,
        email: String,
        password: String,
    ): Result<LoginResponse> =
        runCatching {
            httpClient
                .post("/register") {
                    contentType(ContentType.Application.Json)
                    setBody(RegisterRequest(login, email, password))
                }.body<LoginResponse>()
        }

    override suspend fun deleteAccount(): Result<Boolean> =
        runCatching {
            httpClient
                .delete("/users/me")
                .status == HttpStatusCode.OK
        }
}
