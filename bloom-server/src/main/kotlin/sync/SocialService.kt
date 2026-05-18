package com.example.sync

import com.example.db.tables.UserStatsTable
import com.example.db.tables.UsersTable
import com.example.model.LeaderboardResponse
import com.example.model.LeaderboardUserDto
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

interface SocialService {
    suspend fun getLeaderboard(limit: Int = 100): LeaderboardResponse
}

class SocialServiceImpl : SocialService {
    override suspend fun getLeaderboard(limit: Int): LeaderboardResponse =
        transaction {
            val rows =
                UsersTable
                    .innerJoin(UserStatsTable)
                    .select(UsersTable.columns + UserStatsTable.columns)
                    .limit(limit)
                    .toList()

            val users =
                rows.map { row ->
                    LeaderboardUserDto(
                        id = row[UsersTable.id].value,
                        name = row[UsersTable.nickname],
                        avatarKey = row[UsersTable.avatar],
                        score =
                            computeLeaderboardScore(
                                level = row[UserStatsTable.level],
                                currentExperience = row[UserStatsTable.currentExperience],
                                currentCoinsAmount = row[UserStatsTable.currentCoinsAmount],
                                totalHabitsCompleted = row[UserStatsTable.totalHabitsCompleted],
                                totalTasksCompleted = row[UserStatsTable.totalTasksCompleted],
                                longestStreak = row[UserStatsTable.longestStreak],
                            ),
                    )
                }

            LeaderboardResponse(
                users = users.sortedByDescending { it.score },
                serverTimestamp = System.currentTimeMillis(),
            )
        }

    private fun computeLeaderboardScore(
        level: Int,
        currentExperience: Int,
        currentCoinsAmount: Int,
        totalHabitsCompleted: Int,
        totalTasksCompleted: Int,
        longestStreak: Int,
    ): Long =
        level * 500L +
            currentExperience +
            (currentCoinsAmount / 2) +
            totalHabitsCompleted * 50L +
            totalTasksCompleted * 25L +
            longestStreak * 100L
}
