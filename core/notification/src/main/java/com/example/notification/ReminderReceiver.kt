package com.example.notification

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import timber.log.Timber

class ReminderReceiver : BroadcastReceiver() {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        Timber.d("ReminderReceiver triggered")
        val reminderId = intent.getLongExtra("reminder_id", -1)
        val title = intent.getStringExtra("title") ?: "Напоминание"
        val text = intent.getStringExtra("text") ?: ""

        NotificationFactory.show(
            context = context,
            notificationId = reminderId.toInt(),
            title = title,
            text = text,
        )
    }
}
