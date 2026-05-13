package com.example.data

import com.example.data.repository.HabitRepositoryImpl
import com.example.database.dao.HabitDao
import com.example.database.dao.HabitPlantDao
import com.example.database.dao.HabitReminderDao
import com.example.database.dao.HabitWithRelationDao
import com.example.database.model.entities.HabitEntity
import com.example.database.model.relationships.HabitWithPlantAndReminders
import com.example.database.util.SyncTracker
import com.example.model.HabitWithRelations
import com.example.model.Tag
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
class HabitRepositoryImplTest {
    private val habitDao = mockk<HabitDao>(relaxed = true)
    private val plantDao = mockk<HabitPlantDao>(relaxed = true)
    private val reminderDao = mockk<HabitReminderDao>(relaxed = true)
    private val relationDao = mockk<HabitWithRelationDao>(relaxed = true)
    private val tracker = mockk<SyncTracker>(relaxed = true)
    private lateinit var repository: HabitRepositoryImpl

    @Before
    fun setUp() {
        repository = HabitRepositoryImpl(habitDao, plantDao, reminderDao, relationDao, tracker)
    }

    @Test
    fun `getHabits maps entities to models`() =
        runTest {
            val entity = mockk<HabitEntity>(relaxed = true)
            every { habitDao.getHabits() } returns flowOf(listOf(entity))
            assertEquals(1, repository.getHabits().first().size)
        }

    @Test
    fun `searchHabitsWithRelations filters by tags`() =
        runTest {
            val tag = mockk<Tag>(relaxed = true)
            val habit =
                mockk<HabitWithRelations>(
                    relaxed = true,
                )
            val habitEntity =
                mockk<HabitWithPlantAndReminders>(
                    relaxed = true,
                )
            every { habit.habit.tags } returns setOf(tag)
            every { relationDao.searchHabitsWithRelations(any()) } returns
                flowOf(listOf(habitEntity))
            val result = repository.searchHabitsWithRelations("q", setOf(tag)).first()
            assertTrue(result.isEmpty())
        }

    @Test
    fun `searchHabitsWithRelations returns all when filter empty`() =
        runTest {
            val habit =
                mockk<HabitWithPlantAndReminders>(
                    relaxed = true,
                )
            coEvery { relationDao.searchHabitsWithRelations(any()) } returns flowOf(listOf(habit))
            val result = repository.searchHabitsWithRelations("q", emptySet()).first()
            assertEquals(1, result.size)
        }

    @Test
    fun `getHabitWithRelations returns mapped data`() =
        runTest {
            val relation =
                mockk<HabitWithPlantAndReminders>(
                    relaxed = true,
                )
            every { relation.habitReminders } returns emptyList()
            coEvery { relationDao.getHabitWithPlantAndReminders(1L) } returns relation
            val res = repository.getHabitWithRelations(1L)
            assertEquals(true, res != null)
        }

    @Test
    fun `toggleHabit updates dao with inverted state`() =
        runTest {
            val entity =
                mockk<HabitEntity>(relaxed = true) {
                    every {
                        isChecked
                    } returns false
                }
            coEvery { habitDao.getHabitById(1L) } returns entity
            repository.toggleHabit(1L)
            coVerify { habitDao.toggleHabit(1L, true, tracker) }
        }

    @Test
    fun `deleteHabit calls cascade delete`() =
        runTest {
            val entity = mockk<HabitEntity>(relaxed = true)
            coEvery { habitDao.getHabitById(1L) } returns entity
            repository.deleteHabit(1L)
            coVerify { relationDao.softDeleteHabitCascade(entity, tracker) }
        }

    @Test
    fun `saveHabit delegates to plantDao`() =
        runTest {
            val habit = mockk<com.example.model.Habit>(relaxed = true)
            val plant = mockk<com.example.model.HabitPlant>(relaxed = true)
            coEvery { plantDao.upsertHabitWithPlant(any(), any(), any()) } returns 10L
            val id = repository.saveHabit(habit, plant)
            assertEquals(10L, id)
        }
}
