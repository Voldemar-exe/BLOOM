package com.example.notification

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

object ReminderWorkStarter {
    private const val WORK_NAME = "reminder_sync"

    fun start(context: Context) {
        val request =
            OneTimeWorkRequestBuilder<ReminderWorker>()
                .build()

        WorkManager
            .getInstance(context)
            .enqueueUniqueWork(
                WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                request,
            )
    }
}
