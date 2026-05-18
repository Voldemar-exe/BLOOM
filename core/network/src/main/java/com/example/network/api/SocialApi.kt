package com.example.network.api

import com.example.network.model.LeaderboardResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface SocialApi {
    suspend fun getLeaderboard(limit: Int = 100): Result<LeaderboardResponse>
}

class SocialApiImpl(private val httpClient: HttpClient) : SocialApi {
    override suspend fun getLeaderboard(limit: Int): Result<LeaderboardResponse> =
        runCatching {
            httpClient
                .get("/leaderboard") {
                    parameter("limit", limit)
                }.body<LeaderboardResponse>()
        }
}
