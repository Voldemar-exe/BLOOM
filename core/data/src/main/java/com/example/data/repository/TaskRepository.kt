package com.example.data.repository

import com.example.data.util.asEntity
import com.example.data.util.asModel
import com.example.data.util.asTaskEntity
import com.example.database.dao.SubtaskDao
import com.example.database.dao.TaskDao
import com.example.database.dao.TaskReminderDao
import com.example.database.dao.TaskWithRelationDao
import com.example.database.model.SyncOperation
import com.example.database.model.SyncStatus
import com.example.database.model.SyncTypes
import com.example.database.model.relationships.TaskWithSubtasks
import com.example.database.util.SyncTracker
import com.example.model.Reminder
import com.example.model.Subtask
import com.example.model.Tag
import com.example.model.Task
import com.example.model.TaskWithRelations
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

    suspend fun toggleSubtask(subtaskId: Long): Long?

    suspend fun saveSubtask(subtask: Subtask)

    suspend fun saveReminder(reminder: Reminder)
}

internal class TaskRepositoryImpl(
    val taskDao: TaskDao,
    val subtaskDao: SubtaskDao,
    val taskReminderDao: TaskReminderDao,
    val taskWithRelationDao: TaskWithRelationDao,
    val tracker: SyncTracker,
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
        taskDao.toggleTaskWithSubtasks(taskId, tracker)
    }

    override suspend fun deleteTask(taskId: Long) {
        taskWithRelationDao.softDeleteTaskCascade(taskId, tracker)
    }

    override suspend fun deleteRemindersByIds(remindersToDelete: List<Long>) {
        remindersToDelete.forEach {
            taskReminderDao.deleteById(it)
            tracker.trackSync(SyncTypes.TASK_REMINDER, it, SyncOperation.DELETE)
        }
    }

    override suspend fun deleteSubtasksByIds(subtasksToDelete: List<Long>) {
        subtasksToDelete.forEach {
            subtaskDao.deleteById(it)
            tracker.trackSync(SyncTypes.SUBTASK, it, SyncOperation.DELETE)
        }
    }

    override suspend fun getTaskById(taskId: Long): Task? = taskDao.getTaskById(taskId)?.asModel()

    override suspend fun toggleSubtask(subtaskId: Long): Long? =
        subtaskDao.findById(subtaskId)?.let {
            subtaskDao.upsertWithParentSync(it.copy(isChecked = !it.isChecked), taskDao, tracker)
            taskDao.getTaskById(it.taskId)?.let { task ->
                if (task.isChecked) return@let task.id
                null
            }
        }

    override suspend fun saveTask(task: Task): Long =
        taskDao.upsertWithSync(
            task.asEntity().copy(syncStatus = SyncStatus.CHANGED),
            tracker,
        )

    override suspend fun saveSubtask(subtask: Subtask) {
        taskDao.updateSyncStatus(subtask.taskId, SyncStatus.CHANGED)
        subtaskDao.upsertWithSync(subtask.asEntity(), tracker)
    }

    override suspend fun saveReminder(reminder: Reminder) {
        taskDao.updateSyncStatus(reminder.parentId, SyncStatus.CHANGED)
        taskReminderDao.upsertWithSync(reminder.asTaskEntity(), tracker)
    }
}
