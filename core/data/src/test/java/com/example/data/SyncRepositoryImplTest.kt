package com.example.data

import com.example.data.repository.SyncRepositoryImpl
import com.example.database.dao.SyncDao
import com.example.database.dao.SyncQueueDao
import com.example.database.model.entities.SyncQueueEntity
import com.example.database.util.TransactionRunner
import com.example.network.api.SyncApi
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
class SyncRepositoryImplTest {
    private val queueDao = mockk<SyncQueueDao>(relaxed = true)
    private val syncDao = mockk<SyncDao>(relaxed = true)
    private val api = mockk<SyncApi>(relaxed = true)
    private val transactionRunner =
        mockk<TransactionRunner>(relaxed = true)
    private lateinit var repository: SyncRepositoryImpl

    @Before
    fun setUp() {
        repository = SyncRepositoryImpl(queueDao, syncDao, api, transactionRunner)
    }

    @Test
    fun `observePending maps entities to domain`() =
        runTest {
            val entity = mockk<SyncQueueEntity>(relaxed = true)
            every { queueDao.observeSyncQueue() } returns flowOf(listOf(entity))
            assertEquals(1, repository.observePending().first().size)
        }

    @Test
    fun `pushChanges succeeds on empty queue`() =
        runTest {
            coEvery { queueDao.getPendingList() } returns emptyList()
            val res = repository.pushChanges()
            assertTrue(res.isSuccess)
        }

    @Test
    fun `pushChanges sends data and deletes processed`() =
        runTest {
            val item =
                mockk<SyncQueueEntity>(relaxed = true) {
                    every { entityType } returns
                        com.example.database.model.SyncTypes.HABIT
                    every { entityId } returns 1L
                    every {
                        id
                    } returns 100L
                    every { createdAt } returns 1000L
                }
            coEvery { queueDao.getPendingList() } returns listOf(item)
            coEvery { syncDao.getHabitById(1L) } returns mockk(relaxed = true)
            coEvery { syncDao.getHabitPlantByHabitId(1L) } returns mockk(relaxed = true)
            coEvery { api.push(any()) } returns Result.success(mockk(relaxed = true))
            val res = repository.pushChanges()
            assertTrue(res.isSuccess)
            coVerify { api.push(any()) }
            coVerify { queueDao.deleteByIds(listOf(100L)) }
        }

    @Test
    fun `pullChanges applies response`() =
        runTest {
            val response =
                mockk<com.example.network.model.SyncPullResponse>(relaxed = true) {
                    every { habits } returns emptyList()
                    every { tasks } returns emptyList()
                    every { habitReminders } returns emptyList()
                    every { taskReminders } returns emptyList()
                    every { statsLogs } returns emptyList()
                    every { habitCompletions } returns emptyList()
                    every { taskCompletions } returns emptyList()
                }
            coEvery { api.pull(any()) } returns Result.success(response)
            coEvery { transactionRunner.run<Any?>(any()) } coAnswers {
                firstArg<suspend () -> Any?>().invoke()
            }
            val res = repository.pullChanges(0L)
            assertTrue(res.isSuccess)
            coVerify { transactionRunner.run(any()) }
        }
}
