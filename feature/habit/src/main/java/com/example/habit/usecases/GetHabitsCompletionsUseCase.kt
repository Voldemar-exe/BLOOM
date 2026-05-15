package com.example.habit.usecases

import com.example.data.repository.GamificationRepository
import com.example.model.HabitCompletion
import kotlinx.coroutines.flow.Flow

class GetHabitsCompletionsUseCase(private val gamificationRepository: GamificationRepository) {
    operator fun invoke(): Flow<List<HabitCompletion>> = gamificationRepository.habitsCompletions
}
