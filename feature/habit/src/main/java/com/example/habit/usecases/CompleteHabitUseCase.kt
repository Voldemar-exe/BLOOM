package com.example.habit.usecases

import com.example.data.repository.HabitRepository
import com.example.gamification.GamificationProcessor
import com.example.gamification.model.GamificationEvent

class CompleteHabitUseCase(
    private val habitRepository: HabitRepository,
    private val gamificationProcessor: GamificationProcessor,
) {
    suspend operator fun invoke(habitId: Long): Result<Unit> =
        runCatching {
            habitRepository.toggleHabit(habitId)
            // TODO: Add streak
            gamificationProcessor.processEvent(GamificationEvent.HabitCompleted(habitId, false))
        }
}
