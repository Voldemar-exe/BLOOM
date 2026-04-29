package com.example.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.model.ReminderSchedule
import com.example.notification.util.nextTriggerMillis

interface ReminderScheduler {
    fun schedule(reminder: ReminderSchedule)

    fun cancel(reminderId: Long)

    fun rescheduleAll(reminders: List<ReminderSchedule>)
}

class AlarmReminderScheduler(private val context: Context) : ReminderScheduler {
    private val alarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun schedule(reminder: ReminderSchedule) {
        val triggerAt = reminder.nextTriggerMillis() ?: return

        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAt,
            createPendingIntent(reminder),
        )
    }

    override fun cancel(reminderId: Long) {
        alarmManager.cancel(createPendingIntent(reminderId))
    }

    override fun rescheduleAll(reminders: List<ReminderSchedule>) {
        reminders.forEach(::schedule)
    }

    private fun createPendingIntent(reminder: ReminderSchedule): PendingIntent {
        val intent =
            Intent(context, ReminderReceiver::class.java).apply {
                putExtra("reminder_id", reminder.id)
                putExtra("title", reminder.notificationTitle)
                putExtra("text", reminder.notificationText)
            }

        return PendingIntent.getBroadcast(
            context,
            reminder.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun createPendingIntent(id: Long): PendingIntent {
        val intent =
            Intent(context, ReminderReceiver::class.java).apply {
                putExtra("reminder_id", id)
            }

        return PendingIntent.getBroadcast(
            context,
            id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }
}
