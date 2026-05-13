package com.example.data

import com.example.data.repository.NotificationRepositoryImpl
import com.example.database.dao.HabitReminderDao
import com.example.database.dao.TaskReminderDao
import com.example.database.model.entities.HabitEntity
import com.example.database.model.entities.HabitReminderEntity
import com.example.database.model.relationships.HabitWithReminders
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
class NotificationRepositoryImplTest {
    private val habitDao = mockk<HabitReminderDao>(relaxed = true)
    private val taskDao = mockk<TaskReminderDao>(relaxed = true)
    private lateinit var repository: NotificationRepositoryImpl

    @Before
    fun setUp() {
        repository = NotificationRepositoryImpl(habitDao, taskDao)
    }

    @Test
    fun `getAllSchedules combines and filters enabled`() =
        runTest {
            val hReminder =
                mockk<HabitReminderEntity>(relaxed = true) {
                    every {
                        id
                    } returns 1L
                    every { reminderTime } returns "10:00"
                    every { isEnabled } returns
                        true
                }
            val hHabit =
                mockk<HabitEntity>(relaxed = true) {
                    every { id } returns 1L
                    every { title } returns "H"
                    every { description } returns "D"
                    every {
                        recurrence
                    } returns
                        mockk(
                            relaxed = true,
                        )
                }
            val hWithRem =
                mockk<HabitWithReminders>(
                    relaxed = true,
                ) {
                    every { reminders } returns listOf(hReminder)
                    every { habit } returns hHabit
                }
            every { habitDao.getAllHabitsWithReminders() } returns flowOf(listOf(hWithRem))
            every { taskDao.getAllTasksWithReminders() } returns flowOf(emptyList())
            val res = repository.getAllSchedules().first()
            assertEquals(1, res.size)
            assertTrue(res.first().isEnabled)
        }

    @Test
    fun `getAllSchedules ignores disabled reminders`() =
        runTest {
            val r =
                mockk<HabitReminderEntity>(relaxed = true) {
                    every {
                        id
                    } returns 2L
                    every { reminderTime } returns "10:00"
                    every { isEnabled } returns
                        false
                }
            val h =
                mockk<HabitEntity>(relaxed = true) {
                    every { id } returns 2L
                    every { title } returns "H"
                    every { description } returns "D"
                    every {
                        recurrence
                    } returns
                        mockk(
                            relaxed = true,
                        )
                }
            val wr =
                mockk<HabitWithReminders>(
                    relaxed = true,
                ) {
                    every { reminders } returns listOf(r)
                    every { habit } returns h
                }
            every { habitDao.getAllHabitsWithReminders() } returns flowOf(listOf(wr))
            every { taskDao.getAllTasksWithReminders() } returns flowOf(emptyList())
            val res = repository.getAllSchedules().first()
            assertTrue(res.isEmpty())
        }
}
