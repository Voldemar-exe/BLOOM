package com.example.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.data.repository.NotificationRepository
import kotlinx.coroutines.flow.first
import org.koin.android.annotation.KoinWorker

@KoinWorker
class ReminderWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val repository: NotificationRepository,
    private val scheduler: ReminderScheduler,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result =
        try {
            val reminders = repository.getAllSchedules().first()
            scheduler.rescheduleAll(reminders)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
}
