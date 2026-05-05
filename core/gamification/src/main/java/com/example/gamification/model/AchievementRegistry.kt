package com.example.gamification.model

import com.example.designsystem.picture.BloomAchievementImages

object AchievementRegistry {
    val allAchievements: List<Achievement> =
        listOf(
            Achievement(
                id = 0,
                imageKey = BloomAchievementImages.PLACEHOLDER.name,
                title = "Первая посадка",
                description = "Создать первую привычку",
                condition = HabitCreatedCondition(target = 1),
                rewardXp = 50,
            ),
            Achievement(
                id = 1,
                imageKey = BloomAchievementImages.PLACEHOLDER.name,
                title = "Первая посадка",
                description = "Создать первую привычку",
                condition = HabitCreatedCondition(target = 10),
                rewardXp = 50,
            ),
            Achievement(
                id = 2,
                imageKey = BloomAchievementImages.PLACEHOLDER.name,
                title = "Недельный садовод",
                description = "Поддерживать привычку 7 дней подряд",
                condition = StreakCondition(target = 7),
                rewardXp = 100,
            ),
        )
}
