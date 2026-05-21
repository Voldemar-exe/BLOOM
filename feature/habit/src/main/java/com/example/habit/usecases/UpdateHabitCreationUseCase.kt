package com.example.habit.usecases

import com.example.data.repository.UserRepository
import kotlinx.coroutines.flow.first

class UpdateHabitCreationUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke() {
        userRepository.stats.first().let {
            userRepository.updateStats(it.copy(totalHabitsCreated = it.totalHabitsCreated + 1))
        }
    }
}
