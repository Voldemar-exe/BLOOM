package com.example.data.repository

import com.example.data.util.asEntity
import com.example.data.util.asModel
import com.example.data.util.asTaskEntity
import com.example.database.dao.SubtaskDao
import com.example.database.dao.TaskDao
import com.example.database.dao.TaskReminderDao
import com.example.database.dao.TaskWithRelationDao
import com.example.database.model.relationships.TaskWithSubtasks
import com.example.model.Reminder
import com.example.model.Subtask
import com.example.model.SyncStatus
import com.example.model.Tag
import com.example.model.Task
import com.example.model.TaskWithRelations
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Singleton

@Singleton
interface TaskRepository {
    fun getTasks(): Flow<List<Task>>

    fun searchTasksWithRelations(
        query: String,
        filterTags: Set<Tag>,
    ): Flow<List<TaskWithRelations>>

    suspend fun getTaskWithSubtasks(taskId: Long): TaskWithSubtasks?

    suspend fun getTaskWithRelations(taskId: Long): TaskWithRelations?

    suspend fun getTaskById(taskId: Long): Task?

    suspend fun toggleTask(taskId: Long)

    suspend fun deleteTask(taskId: Long)

    suspend fun deleteRemindersByIds(remindersToDelete: List<Long>)

    suspend fun deleteSubtasksByIds(subtasksToDelete: List<Long>)

    suspend fun saveTask(task: Task): Long

    suspend fun toggleSubtask(subtaskId: Long)

    suspend fun saveSubtask(subtask: Subtask)

    suspend fun saveReminder(reminder: Reminder)
}

@Singleton
internal class TaskRepositoryImpl(
    val taskDao: TaskDao,
    val subtaskDao: SubtaskDao,
    val taskReminderDao: TaskReminderDao,
    val taskWithRelationDao: TaskWithRelationDao,
) : TaskRepository {
    override fun getTasks(): Flow<List<Task>> =
        taskDao.getTasks().map { entities -> entities.map { it.asModel() } }

    override fun searchTasksWithRelations(
        query: String,
        filterTags: Set<Tag>,
    ): Flow<List<TaskWithRelations>> =
        taskWithRelationDao
            .searchTasksWithRelations(query)
            .map { entities -> entities.map { it.asModel() } }
            .map { tasks ->
                if (filterTags.isEmpty()) {
                    tasks
                } else {
                    tasks.filter {
                        it.task.tags.containsAll(filterTags)
                    }
                }
            }

    override suspend fun getTaskWithSubtasks(taskId: Long): TaskWithSubtasks? =
        taskWithRelationDao.getTaskWithSubtasks(taskId)

    override suspend fun getTaskWithRelations(taskId: Long): TaskWithRelations? =
        taskWithRelationDao.getTaskWithSubtasksAndReminders(taskId)?.let { taskInfo ->
            TaskWithRelations(
                taskInfo.taskEntity.asModel(),
                taskInfo.subtaskEntities.map { it.asModel() },
                taskInfo.taskReminderEntities.map { it.asModel() },
            )
        }

    override suspend fun toggleTask(taskId: Long) {
        taskDao.toggleTaskWithSubtasks(taskId)
    }

    override suspend fun deleteTask(taskId: Long) {
        taskWithRelationDao.softDeleteTaskCascade(taskId)
    }

    override suspend fun deleteRemindersByIds(remindersToDelete: List<Long>) {
        remindersToDelete.forEach { taskReminderDao.deleteById(it) }
    }

    override suspend fun deleteSubtasksByIds(subtasksToDelete: List<Long>) {
        subtasksToDelete.forEach { subtaskDao.deleteById(it) }
    }

    override suspend fun getTaskById(taskId: Long): Task? = taskDao.getTaskById(taskId)?.asModel()

    override suspend fun toggleSubtask(subtaskId: Long) {
        subtaskDao.findById(subtaskId)?.let {
            subtaskDao.updateWithParentSync(it.copy(isChecked = !it.isChecked), taskDao)
        }
    }

    override suspend fun saveTask(task: Task): Long =
        taskDao.upsert(task.asEntity().copy(syncStatus = SyncStatus.CHANGED))

    override suspend fun saveSubtask(subtask: Subtask) {
        taskDao.updateSyncStatus(subtask.taskId, SyncStatus.CHANGED)
        subtaskDao.upsert(subtask.asEntity())
    }

    override suspend fun saveReminder(reminder: Reminder) {
        taskDao.updateSyncStatus(reminder.parentId, SyncStatus.CHANGED)
        taskReminderDao.upsert(reminder.asTaskEntity())
    }
}
