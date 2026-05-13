package com.example

import com.example.db.daos.HabitDAO
import com.example.db.daos.StatsLogDAO
import com.example.db.daos.TaskDAO
import com.example.db.daos.UserDAO
import com.example.model.SyncPushRequest
import kotlinx.coroutines.test.runTest
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import sync.SyncService
import sync.SyncServiceImpl
import kotlin.test.Test

class SyncServiceIntegrationTest {
    private lateinit var service: SyncService

    @Before
    fun setup() {
        TestDatabaseFactory.init()
        service = SyncServiceImpl()

        transaction {
            UserDAO.new(1L) {
                login = "testUser"
                email = "test@test.com"
                passwordHash = "hash"
                nickname = "testUser"
                createdAt = System.currentTimeMillis()
                updatedAt = System.currentTimeMillis()
            }
        }
    }

    @After
    fun tearDown() {
        TestDatabaseFactory.clear()
    }

    @Test
    fun push_savesHabitsAndTasks() =
        runTest {
            val request =
                SyncPushRequest(
                    habits = listOf(TestData.habitDto(id = 1)),
                    tasks = listOf(TestData.taskDto(id = 1)),
                    habitReminders = emptyList(),
                    taskReminders = emptyList(),
                    statsLogs = emptyList(),
                    habitCompletions = emptyList(),
                    taskCompletions = emptyList(),
                )

            val result = service.push(userId = 1L, request = request)

            assertTrue(result.isSuccess)

            transaction {
                assertEquals(1L, HabitDAO.all().count())
                assertEquals(1L, TaskDAO.all().count())
            }
        }

    @Test
    fun pull_returnsOnlyEntitiesUpdatedAfterTimestamp() =
        runTest {
            val ts = System.currentTimeMillis()

            service.push(
                userId = 1L,
                request =
                    SyncPushRequest(
                        habits =
                            listOf(
                                TestData.habitDto(
                                    id = 1,
                                    updatedAt = ts + 1000,
                                ),
                            ),
                        tasks = emptyList(),
                        habitReminders = emptyList(),
                        taskReminders = emptyList(),
                        statsLogs = emptyList(),
                        habitCompletions = emptyList(),
                        taskCompletions = emptyList(),
                    ),
            )

            val response =
                service.pull(
                    userId = 1L,
                    lastSyncTimestamp = ts,
                )

            assertEquals(1, response.habits.size)
            assertEquals(0, response.tasks.size)
        }

    @Test
    fun push_doesNotDuplicateStatsLogsByEventId() =
        runTest {
            val log = TestData.statsLogDto(eventId = "same_event")

            val request =
                SyncPushRequest(
                    habits = emptyList(),
                    tasks = emptyList(),
                    habitReminders = emptyList(),
                    taskReminders = emptyList(),
                    statsLogs = listOf(log),
                    habitCompletions = emptyList(),
                    taskCompletions = emptyList(),
                )

            service.push(1L, request)
            service.push(1L, request)

            transaction {
                assertEquals(1L, StatsLogDAO.all().count())
            }
        }
}
