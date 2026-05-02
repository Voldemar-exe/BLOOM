package com.example.task.usecases

import com.example.data.repository.TaskRepository
import com.example.gamification.GamificationProcessor
import com.example.gamification.model.GamificationEvent

class CompleteTaskUseCase(
    private val taskRepository: TaskRepository,
    private val gamificationProcessor: GamificationProcessor,
) {
    suspend operator fun invoke(
        taskId: Long,
        calledBySubtask: Boolean = false,
    ): Result<Unit> =
        runCatching {
            if (!calledBySubtask) taskRepository.toggleTask(taskId)
            gamificationProcessor.processEvent(GamificationEvent.TaskCompleted(taskId))
        }
}
