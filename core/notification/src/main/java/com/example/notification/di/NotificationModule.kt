package com.example.notification.di

import com.example.notification.AlarmReminderScheduler
import com.example.notification.NotificationManager
import com.example.notification.ReminderScheduler
import org.koin.dsl.module

val notificationModule =
    module {
        single<ReminderScheduler> { AlarmReminderScheduler(context = get()) }

        single<NotificationManager> { NotificationManager(scheduler = get()) }
    }
