package com.example.data.repository

import com.example.data.util.stringToLocalTime
import com.example.database.dao.HabitReminderDao
import com.example.database.dao.TaskReminderDao
import com.example.model.ReminderSchedule
import com.example.model.ReminderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

interface NotificationRepository {
    fun getAllSchedules(): Flow<List<ReminderSchedule>>
}

internal class NotificationRepositoryImpl(
    private val habitReminderDao: HabitReminderDao,
    private val taskReminderDao: TaskReminderDao,
) : NotificationRepository {
    override fun getAllSchedules(): Flow<List<ReminderSchedule>> {
        val habitsFlow = habitReminderDao.getAllHabitsWithReminders()
        val tasksFlow = taskReminderDao.getAllTasksWithReminders()

        return combine(habitsFlow, tasksFlow) { habits, tasks ->
            buildList {
                habits.forEach { habit ->
                    habit.reminders.forEach { reminder ->
                        add(
                            ReminderSchedule(
                                id = reminder.id,
                                parentId = habit.habit.id,
                                title = habit.habit.title,
                                description = habit.habit.description,
                                time = stringToLocalTime(reminder.reminderTime),
                                recurrence = habit.habit.recurrence,
                                isEnabled = reminder.isEnabled,
                                type = ReminderType.HABIT,
                            ),
                        )
                    }
                }

                tasks.forEach { task ->
                    task.reminders.forEach { reminder ->
                        add(
                            ReminderSchedule(
                                id = reminder.id,
                                parentId = task.task.id,
                                title = task.task.title,
                                description = task.task.description,
                                time = stringToLocalTime(reminder.reminderTime),
                                recurrence = task.task.recurrence,
                                isEnabled = reminder.isEnabled,
                                type = ReminderType.TASK,
                            ),
                        )
                    }
                }
            }.filter { it.isEnabled }
        }
    }
}
