package com.example.sync

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object SyncScheduler {
    private const val SYNC_PERIODIC_WORK_NAME = "bloom_sync_periodic"
    private const val SYNC_IMMEDIATE_WORK_NAME = "bloom_sync_immediate"

    fun enqueuePeriodicSync(context: Context) {
        val constraints =
            Constraints
                .Builder()
//                .setRequiredNetworkType(NetworkType.CONNECTED) TODO: For prod
                .setRequiresBatteryNotLow(true)
                .build()

        val workRequest =
            PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.SECONDS)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
                .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            SYNC_PERIODIC_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest,
        )
    }

    fun enqueueImmediateSync(context: Context) {
        val constraints =
            Constraints
                .Builder()
//                .setRequiredNetworkType(NetworkType.CONNECTED) TODO: For prod
                .build()

        val workRequest =
            OneTimeWorkRequestBuilder<SyncWorker>()
//                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.SECONDS)
                .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            SYNC_IMMEDIATE_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            workRequest,
        )
    }
}
