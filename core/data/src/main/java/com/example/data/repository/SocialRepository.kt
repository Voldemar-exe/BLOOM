package com.example.data.repository

import com.example.data.util.toModel
import com.example.datastore.datastore.LeaderboardDataStore
import com.example.model.LeaderboardUser
import com.example.network.api.SocialApi

interface SocialRepository {
    suspend fun getLeaderboard(forceRefresh: Boolean = false): List<LeaderboardUser>
}

class SocialRepositoryImpl(
    private val api: SocialApi,
    private val leaderboardDataStore: LeaderboardDataStore,
) : SocialRepository {
    override suspend fun getLeaderboard(forceRefresh: Boolean): List<LeaderboardUser> {
        if (!forceRefresh && leaderboardDataStore.isFresh()) {
            return leaderboardDataStore.getUsers()
        }

        return runCatching {
            val response = api.getLeaderboard().getOrThrow()

            val users = response.users.map { it.toModel() }

            leaderboardDataStore.save(
                users = users,
                timestamp = response.serverTimestamp,
            )

            users
        }.getOrElse {
            leaderboardDataStore.getUsers()
        }
    }
}
