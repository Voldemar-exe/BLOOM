package com.example.notification.di

import com.example.notification.AlarmReminderScheduler
import com.example.notification.ReminderScheduler
import com.example.notification.ReminderWorker
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val notificationModule =
    module {
        single<ReminderScheduler> {
            AlarmReminderScheduler(get())
        }
        worker<ReminderWorker> {
            ReminderWorker(
                appContext = get(),
                workerParams = get(),
                repository = get(),
                scheduler = get(),
            )
        }
    }
