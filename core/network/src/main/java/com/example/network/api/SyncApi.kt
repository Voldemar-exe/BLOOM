package com.example.network.api

import android.accounts.NetworkErrorException
import com.example.network.model.SyncPullResponse
import com.example.network.model.SyncPushRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

interface SyncApi {
    suspend fun push(request: SyncPushRequest): Result<Unit>

    suspend fun pull(lastSyncTimestamp: Long): Result<SyncPullResponse>
}

class SyncApiImpl(private val httpClient: HttpClient) : SyncApi {
    override suspend fun push(request: SyncPushRequest): Result<Unit> =
        runCatching {
            httpClient
                .post("/sync/push") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }.apply {
                    if (status != HttpStatusCode.OK) {
                        throw NetworkErrorException("status: ${status.value}, body: ${bodyAsText()}")
                    }
                }
        }

    override suspend fun pull(lastSyncTimestamp: Long): Result<SyncPullResponse> =
        runCatching {
            httpClient
                .get("/sync/pull") {
                    parameter("lastSync", lastSyncTimestamp)
                }.body<SyncPullResponse>()
        }
}
