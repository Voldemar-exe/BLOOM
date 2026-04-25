package com.example.data.repository

import com.example.data.util.asEntity
import com.example.data.util.asHabitEntity
import com.example.data.util.asModel
import com.example.database.dao.HabitDao
import com.example.database.dao.HabitPlantDao
import com.example.database.dao.HabitReminderDao
import com.example.database.dao.HabitWithRelationDao
import com.example.model.Habit
import com.example.model.HabitPlant
import com.example.model.HabitWithRelations
import com.example.model.Reminder
import com.example.model.SyncStatus
import com.example.model.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Singleton

@Singleton
interface HabitRepository {
    fun getHabits(): Flow<List<Habit>>

    fun searchHabitsWithRelations(
        query: String,
        filterTags: Set<Tag>,
    ): Flow<List<HabitWithRelations>>

    suspend fun getHabitWithRelations(habitId: Long): HabitWithRelations?

    suspend fun getHabitById(habitId: Long): Habit?

    suspend fun toggleHabit(habitId: Long)

    suspend fun deleteHabit(habitId: Long)

    suspend fun deleteRemindersByIds(ids: List<Long>)

    suspend fun saveHabit(habit: Habit): Long

    suspend fun savePlant(plant: HabitPlant)

    suspend fun saveReminder(reminder: Reminder)
}

@Singleton
internal class HabitRepositoryImpl(
    private val habitDao: HabitDao,
    private val plantDao: HabitPlantDao,
    private val reminderDao: HabitReminderDao,
    private val relationDao: HabitWithRelationDao,
) : HabitRepository {
    override fun getHabits(): Flow<List<Habit>> =
        habitDao.getHabits().map { it.map { entity -> entity.asModel() } }

    override fun searchHabitsWithRelations(
        query: String,
        filterTags: Set<Tag>,
    ): Flow<List<HabitWithRelations>> =
        relationDao
            .searchHabitsWithRelations(query)
            .map { entities -> entities.map { it.asModel() } }
            .map { tasks ->
                if (filterTags.isEmpty()) {
                    tasks
                } else {
                    tasks.filter {
                        it.habit.tags.containsAll(filterTags)
                    }
                }
            }

    override suspend fun getHabitWithRelations(habitId: Long): HabitWithRelations? =
        relationDao.getHabitWithPlantAndReminders(habitId)?.let { habitInfo ->
            HabitWithRelations(
                habitInfo.habit.asModel(),
                habitInfo.plant.asModel(),
                habitInfo.habitReminders.map { it.asModel() },
            )
        }

    override suspend fun getHabitById(habitId: Long): Habit? =
        habitDao.getHabitById(habitId)?.asModel()

    override suspend fun toggleHabit(habitId: Long) {
        habitDao.getHabitById(habitId)?.let {
            habitDao.updateHabitCompletion(habitId, !it.isChecked)
        }
    }

    override suspend fun deleteHabit(habitId: Long) {
        relationDao.softDeleteHabitCascade(habitId)
    }

    override suspend fun deleteRemindersByIds(ids: List<Long>) {
        ids.forEach { reminderDao.deleteById(it) }
    }

    override suspend fun saveHabit(habit: Habit): Long =
        habitDao.upsert(habit.asEntity().copy(syncStatus = SyncStatus.CHANGED))

    override suspend fun savePlant(plant: HabitPlant) {
        habitDao.updateSyncStatus(plant.habitId, SyncStatus.CHANGED)
        plantDao.upsert(plant.asEntity())
    }

    override suspend fun saveReminder(reminder: Reminder) {
        habitDao.updateSyncStatus(reminder.parentId, SyncStatus.CHANGED)
        reminderDao.upsert(reminder.asHabitEntity())
    }
}
