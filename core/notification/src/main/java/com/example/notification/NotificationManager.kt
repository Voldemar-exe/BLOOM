package com.example.notification

import com.example.model.AppSettings
import com.example.model.ReminderSchedule
import com.example.model.ReminderType

class NotificationManager(private val scheduler: ReminderScheduler) {
    private var currentReminders: Map<Long, ReminderSchedule> = emptyMap()

    fun sync(
        settings: AppSettings,
        reminders: List<ReminderSchedule>,
    ) {
        val filteredReminders =
            reminders.filter { reminder ->
                when (reminder.type) {
                    ReminderType.HABIT ->
                        settings.pushEnabled &&
                            settings.habitRemindersEnabled

                    ReminderType.TASK ->
                        settings.pushEnabled &&
                            settings.taskRemindersEnabled
                }
            }
        syncInternal(filteredReminders)
    }

    private fun syncInternal(newReminders: List<ReminderSchedule>) {
        val newMap = newReminders.associateBy { it.id }

        val oldIds = currentReminders.keys
        val newIds = newMap.keys

        val removedIds = oldIds - newIds
        val addedIds = newIds - oldIds
        val commonIds = oldIds intersect newIds

        removedIds.forEach { id ->
            scheduler.cancel(id)
        }

        addedIds.forEach { id ->
            scheduler.schedule(newMap.getValue(id))
        }

        commonIds.forEach { id ->
            val oldReminder = currentReminders[id]
            val newReminder = newMap[id]

            if (newReminder != null && oldReminder?.isScheduleEquivalent(newReminder) == false) {
                scheduler.cancel(id)
                scheduler.schedule(newReminder)
            }
        }

        currentReminders = newMap
    }
}

private fun ReminderSchedule.isScheduleEquivalent(other: ReminderSchedule): Boolean =
    id == other.id &&
        time == other.time &&
        recurrence == other.recurrence &&
        isEnabled == other.isEnabled
