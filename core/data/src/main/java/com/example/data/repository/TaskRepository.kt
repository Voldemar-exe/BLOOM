package com.example.data.repository

import com.example.database.dao.SubtaskDao
import com.example.database.dao.TaskDao
import com.example.database.dao.TaskReminderDao
import com.example.database.dao.TaskWithRelationDao
import com.example.database.model.Subtask
import com.example.database.model.Task
import com.example.database.model.TaskReminder
import com.example.database.model.relationships.TaskAndSubtask
import com.example.model.SyncStatus
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Singleton

@Singleton
interface TaskRepository {
    fun getTasks(): Flow<List<Task>>

    fun getTaskWithSubtasks(taskId: Long): Flow<TaskAndSubtask?>

    suspend fun saveTask(task: Task)

    suspend fun saveSubtask(subtask: Subtask)

    suspend fun saveReminder(reminder: TaskReminder)
}

@Singleton
internal class TaskRepositoryImpl(
    val taskDao: TaskDao,
    val subtaskDao: SubtaskDao,
    val taskReminderDao: TaskReminderDao,
    val taskWithRelationDao: TaskWithRelationDao,
) : TaskRepository {
    override fun getTasks(): Flow<List<Task>> = taskDao.getTasks()

    override fun getTaskWithSubtasks(taskId: Long): Flow<TaskAndSubtask?> =
        taskWithRelationDao.getTaskWithSubtasks(taskId)

    override suspend fun saveTask(task: Task) {
        taskDao.upsert(task.copy(syncStatus = SyncStatus.CHANGED))
    }

    override suspend fun saveSubtask(subtask: Subtask) {
        taskDao.updateSyncStatus(subtask.taskId, SyncStatus.CHANGED)
        subtaskDao.upsert(subtask)
    }

    override suspend fun saveReminder(reminder: TaskReminder) {
        taskDao.updateSyncStatus(reminder.taskId, SyncStatus.CHANGED)
        taskReminderDao.upsert(reminder)
    }
}
