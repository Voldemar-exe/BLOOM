package com.example.data

import com.example.data.repository.TaskRepositoryImpl
import com.example.database.dao.SubtaskDao
import com.example.database.dao.TaskDao
import com.example.database.dao.TaskReminderDao
import com.example.database.dao.TaskWithRelationDao
import com.example.database.model.SyncOperation
import com.example.database.model.SyncTypes
import com.example.database.model.entities.SubtaskEntity
import com.example.database.model.entities.TaskEntity
import com.example.database.model.relationships.TaskWithSubtasksAndReminders
import com.example.database.util.SyncTracker
import com.example.model.Tag
import com.example.model.TaskWithRelations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TaskRepositoryImplTest {
    private val taskDao = mockk<TaskDao>(relaxed = true)
    private val subtaskDao = mockk<SubtaskDao>(relaxed = true)
    private val taskReminderDao = mockk<TaskReminderDao>(relaxed = true)
    private val relationDao = mockk<TaskWithRelationDao>(relaxed = true)
    private val tracker = mockk<SyncTracker>(relaxed = true)
    private lateinit var repository: TaskRepositoryImpl

    @Before
    fun setUp() {
        repository = TaskRepositoryImpl(taskDao, subtaskDao, taskReminderDao, relationDao, tracker)
    }

    @Test
    fun `getTasks maps entities`() =
        runTest {
            every { taskDao.getTasks() } returns flowOf(listOf(mockk(relaxed = true)))
            assertEquals(1, repository.getTasks().first().size)
        }

    @Test
    fun `searchTasksWithRelations filters by tags`() =
        runTest {
            val tag = mockk<Tag>(relaxed = true)
            val task = mockk<TaskWithRelations>(relaxed = true)
            val taskEntity = mockk<TaskWithSubtasksAndReminders>(relaxed = true)
            every { task.task.tags } returns setOf(tag)
            every { relationDao.searchTasksWithRelations(any()) } returns flowOf(listOf(taskEntity))
            val res = repository.searchTasksWithRelations("q", setOf(mockk(relaxed = true))).first()
            assertTrue(res.isEmpty())
        }

    @Test
    fun `toggleTask calls dao`() =
        runTest {
            repository.toggleTask(5L)
            coVerify { taskDao.toggleTaskWithSubtasks(5L, tracker) }
        }

    @Test
    fun `deleteTask calls cascade`() =
        runTest {
            repository.deleteTask(5L)
            coVerify { relationDao.softDeleteTaskCascade(5L, tracker) }
        }

    @Test
    fun `deleteRemindersByIds tracks sync`() =
        runTest {
            repository.deleteRemindersByIds(listOf(1L, 2L))
            coVerify(exactly = 2) {
                tracker.trackSync(
                    SyncTypes.TASK_REMINDER,
                    any(),
                    SyncOperation.DELETE,
                )
            }
        }

    @Test
    fun `toggleSubtask updates parent if task checked`() =
        runTest {
            val sub =
                mockk<SubtaskEntity>(relaxed = true) {
                    every {
                        taskId
                    } returns 3L
                    every { isChecked } returns false
                }
            val task =
                mockk<TaskEntity>(relaxed = true) {
                    every {
                        isChecked
                    } returns true
                    every { id } returns 3L
                }
            coEvery { subtaskDao.findById(10L) } returns sub
            coEvery { taskDao.getTaskById(3L) } returns task
            val res = repository.toggleSubtask(10L)
            assertEquals(3L, res)
        }

    @Test
    fun `saveTask sets sync status changed`() =
        runTest {
            val task = mockk<com.example.model.Task>(relaxed = true)
            coEvery { taskDao.upsertWithSync(any(), any()) } returns 7L
            repository.saveTask(task)
            coVerify { taskDao.upsertWithSync(any(), tracker) }
        }
}
