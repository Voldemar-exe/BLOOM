package com.example.model.util

import com.example.model.Reward
import timber.log.Timber
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
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
        Timber.d(
            "Start: current=$currentStreak, longest=$longestStreak, ts=$lastCompletionTimestamp",
        )
        if (lastCompletionTimestamp == null) return 1 to maxOf(currentStreak, 1)

        return try {
            val lastDate =
                Instant.ofEpochMilli(lastCompletionTimestamp).atZone(ZoneId.systemDefault())
            val today = LocalDate.now(ZoneId.systemDefault())
            val daysDiff = ChronoUnit.DAYS.between(lastDate.toLocalDate(), today)

            Timber.d("Calculated diff: $daysDiff")

            when {
                daysDiff == 0L -> currentStreak to longestStreak
                daysDiff == 1L -> (currentStreak + 1) to maxOf(longestStreak, currentStreak + 1)
                else -> 1 to maxOf(longestStreak, 1)
            }
        } catch (e: Throwable) {
            Timber.e(e, "Streak calculation failed")
            1 to maxOf(longestStreak, 1)
        }
    }
}

object XpRules {
    private const val BASE_XP = 100

    fun xpToNextLevel(level: Int): Int = BASE_XP * level

    fun calculateLevel(totalXp: Int): Int {
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
