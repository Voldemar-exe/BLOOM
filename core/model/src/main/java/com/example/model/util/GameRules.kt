package com.example.model.util

import com.example.model.Reward
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

// TODO: Think about right amount of rewards
object RewardRules {
    private const val BASE_HABIT_XP = 10
    private const val BASE_HABIT_COINS = 5
    private const val STREAK_BONUS_MULTIPLIER = 1.5f
    private const val BASE_TASK_XP = 5
    private const val BASE_TASK_COINS = 1

    fun calculateHabitReward(isStreak: Boolean): Reward {
        val xp = (BASE_HABIT_XP * if (isStreak) STREAK_BONUS_MULTIPLIER else 1f).toInt()
        val coins = (BASE_HABIT_COINS * if (isStreak) STREAK_BONUS_MULTIPLIER else 1f).toInt()
        return Reward(experience = xp, coins = coins)
    }

    fun calculateTaskReward(): Reward =
        Reward(
            experience = BASE_TASK_XP,
            coins = BASE_TASK_COINS,
        )
}

object StreakRules {
    fun calculateStreak(
        lastCompletionTimestamp: Long?,
        currentStreak: Int,
        longestStreak: Int,
    ): Pair<Int, Int> {
        if (lastCompletionTimestamp == null) return 1 to maxOf(currentStreak, 1)

        val lastDate =
            ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(lastCompletionTimestamp),
                ZoneId.systemDefault(),
            )
        val today = LocalDate.now(ZoneId.systemDefault())
        val daysDiff = ChronoUnit.DAYS.between(lastDate, today)

        return when {
            daysDiff <= 1 -> (currentStreak + 1) to maxOf(longestStreak, currentStreak + 1)
            daysDiff > 1 -> 1 to longestStreak
            else -> currentStreak to longestStreak
        }
    }
}

object XpRules {
    private const val BASE_XP = 100

    fun xpToNextLevel(level: Int): Long = BASE_XP * level.toLong()

    fun calculateLevel(totalXp: Long): Int {
        var remaining = totalXp
        var level = 1
        while (remaining >= xpToNextLevel(level)) {
            remaining -= xpToNextLevel(level)
            level++
        }
        return level
    }

    fun calculateRemainingXp(
        totalXp: Long,
        level: Int,
    ): Long {
        var remaining = totalXp
        for (l in 1 until level) {
            remaining -= xpToNextLevel(l)
        }
        return remaining.coerceAtLeast(0)
    }
}
