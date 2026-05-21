package com.example.task.usecases

import com.example.data.repository.UserRepository
import kotlinx.coroutines.flow.first

class UpdateTaskCreationUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke() {
        userRepository.stats.first().let {
            userRepository.updateStats(it.copy(totalTasksCreated = it.totalTasksCreated + 1))
        }
    }
}
