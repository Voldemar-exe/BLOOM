package com.example.data.repository

import com.example.data.util.asEntity
import com.example.data.util.asHabitEntity
import com.example.data.util.asModel
import com.example.database.dao.HabitDao
import com.example.database.dao.HabitPlantDao
import com.example.database.dao.HabitReminderDao
import com.example.database.dao.HabitWithRelationDao
import com.example.database.model.SyncOperation
import com.example.database.model.SyncTypes
import com.example.database.util.SyncTracker
import com.example.model.Habit
import com.example.model.HabitPlant
import com.example.model.HabitWithRelations
import com.example.model.Reminder
import com.example.model.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

    suspend fun saveHabit(
        habit: Habit,
        plant: HabitPlant,
    ): Long

    suspend fun saveReminder(reminder: Reminder)
}

internal class HabitRepositoryImpl(
    private val habitDao: HabitDao,
    private val plantDao: HabitPlantDao,
    private val reminderDao: HabitReminderDao,
    private val relationDao: HabitWithRelationDao,
    private val tracker: SyncTracker,
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
            .map { habits ->
                if (filterTags.isEmpty()) {
                    habits
                } else {
                    habits.filter {
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
            habitDao.toggleHabit(habitId, !it.isChecked, tracker)
        }
    }

    override suspend fun deleteHabit(habitId: Long) {
        relationDao.softDeleteHabitCascade(habitDao.getHabitById(habitId)!!, tracker)
    }

    override suspend fun deleteRemindersByIds(ids: List<Long>) {
        ids.forEach {
            reminderDao.deleteById(it)
            tracker.trackSync(SyncTypes.HABIT_REMINDER, it, SyncOperation.DELETE)
        }
    }

    override suspend fun saveHabit(
        habit: Habit,
        plant: HabitPlant,
    ): Long = plantDao.upsertHabitWithPlant(habit.asEntity(), plant.asEntity(), tracker)

    override suspend fun saveReminder(reminder: Reminder) {
        reminderDao.upsertWithSync(reminder.asHabitEntity(), tracker)
    }
}
