package com.example.data

import com.example.data.repository.GamificationRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class GamificationRepositoryImplTest {
    private val dao = mockk<com.example.database.dao.GamificationDao>(relaxed = true)
    private val tracker = mockk<com.example.database.util.SyncTracker>(relaxed = true)
    private lateinit var repository: GamificationRepositoryImpl

    @Before
    fun setUp() {
        repository = GamificationRepositoryImpl(dao, tracker)
    }

    @Test
    fun `recordHabitCompletion inserts completion and stats log`() =
        runTest {
            coEvery { dao.insertHabitCompletionWithSync(any(), any()) } returns Unit
            coEvery { dao.insertStatsLogWithSync(any(), any()) } returns Unit
            repository.recordHabitCompletion(1L, 10, 5)
            coVerify { dao.insertHabitCompletionWithSync(any(), tracker) }
            coVerify { dao.insertStatsLogWithSync(any(), tracker) }
        }

    @Test
    fun `recordHabitCompletion throws on negative XP`() =
        runTest {
            try {
                repository.recordHabitCompletion(1L, -5, 0)
                throw AssertionError("Expected IllegalArgumentException")
            } catch (e: IllegalArgumentException) {
            }
        }

    @Test
    fun `recordTaskCompletion inserts completion and stats log`() =
        runTest {
            coEvery { dao.insertTaskCompletionWithSync(any(), any()) } returns Unit
            coEvery { dao.insertStatsLogWithSync(any(), any()) } returns Unit
            repository.recordTaskCompletion(2L, 15, 3)
            coVerify { dao.insertTaskCompletionWithSync(any(), tracker) }
        }

    @Test
    fun `isHabitCompletedToday returns true if exists`() =
        runTest {
            coEvery { dao.hasHabitCompletionToday(any(), any(), any()) } returns true
            assertTrue(repository.isHabitCompletedToday(1L))
        }

    @Test
    fun `isHabitCompletedToday returns false if not exists`() =
        runTest {
            coEvery { dao.hasHabitCompletionToday(any(), any(), any()) } returns false
            assertFalse(repository.isHabitCompletedToday(1L))
        }

    @Test
    fun `isTaskCompletedToday returns true if exists`() =
        runTest {
            coEvery { dao.hasTaskCompletionToday(any(), any(), any()) } returns true
            assertTrue(repository.isTaskCompletedToday(5L))
        }
}
