package com.example.notification

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.bloom.core.notification.R

object NotificationFactory {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun show(
        context: Context,
        notificationId: Int,
        title: String,
        text: String,
    ) {
        NotificationManagerCompat
            .from(context)
            .notify(
                notificationId,
                NotificationCompat
                    .Builder(context, "bloom_reminders")
                    .setSmallIcon(R.drawable.alarm_24px)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build(),
            )
    }
}
