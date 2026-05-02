package com.example.gamification

import com.example.data.repository.GamificationRepository
import com.example.data.repository.UserRepository
import com.example.gamification.model.GamificationEvent
import com.example.model.Reward
import com.example.model.util.RewardRules
import com.example.model.util.StreakRules
import com.example.model.util.XpRules
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Singleton
import timber.log.Timber

interface GamificationProcessor {
    suspend fun processEvent(event: GamificationEvent)
}

@Singleton
class GamificationManager(
    private val userRepository: UserRepository,
    private val gamificationRepository: GamificationRepository,
) : GamificationProcessor {
    override suspend fun processEvent(event: GamificationEvent) {
        val reward =
            when (event) {
                is GamificationEvent.HabitCompleted -> {
                    if (gamificationRepository.isHabitCompletedToday(event.habitId)) return
                    val baseReward = RewardRules.calculateHabitReward(event.isStreak)
                    gamificationRepository.recordHabitCompletion(
                        habitId = event.habitId,
                        experienceEarned = baseReward.experience,
                        coinsEarned = baseReward.coins,
                    )
                    baseReward
                }

                is GamificationEvent.TaskCompleted -> {
                    if (gamificationRepository.isTaskCompletedToday(event.taskId)) return
                    val baseReward = RewardRules.calculateTaskReward()
                    gamificationRepository.recordTaskCompletion(
                        taskId = event.taskId,
                        experienceEarned = baseReward.experience,
                        coinsEarned = baseReward.coins,
                    )
                    baseReward
                }

                is GamificationEvent.AchievementEarned -> {
                    userRepository.addAchievement(event.achievementId)
                    Reward(
                        experience = 50,
                        coins = 25,
                        achievementUnlocked = event.achievementId,
                    )
                }
            }

        applyReward(reward)
        Timber.d("Processed $event → $reward")
    }

    private suspend fun applyReward(reward: Reward) {
        val currentStats = userRepository.stats.first()
        val newTotalXp = currentStats.currentExperience + reward.experience
        val currentLevel = XpRules.calculateLevel(newTotalXp)
        val newCoinsAmount = currentStats.currentCoinsAmount + reward.coins

        val updatedStats =
            currentStats.copy(
                currentExperience = newTotalXp,
                level = currentLevel,
                currentCoinsAmount = newCoinsAmount,
                maxCoinsAmount =
                    if (newCoinsAmount >
                        currentStats.maxCoinsAmount
                    ) {
                        newCoinsAmount
                    } else {
                        currentStats.maxCoinsAmount
                    },
                totalHabitsCompleted =
                    currentStats.totalHabitsCompleted +
                        if (reward.experience > 0 && reward.coins > 0) 1 else 0,
            )

        userRepository.updateStats(updatedStats)
    }

    suspend fun updateStreaks(lastHabitCompletion: Long?) {
        val stats = userRepository.stats.first()
        val (newCurrent, newLongest) =
            StreakRules.calculateStreak(
                lastCompletionTimestamp = lastHabitCompletion,
                currentStreak = stats.currentStreak,
                longestStreak = stats.longestStreak,
            )
        userRepository.updateStats(
            stats.copy(
                currentStreak = newCurrent,
                longestStreak = newLongest,
            ),
        )
    }
}
