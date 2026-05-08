package com.example.bloom

import android.Manifest
import android.app.Application
import androidx.annotation.RequiresPermission
import com.example.auth.di.authModule
import com.example.bloom.di.appModule
import com.example.bloom.ui.createNotificationChannel
import com.example.data.di.dataModule
import com.example.database.di.databaseModule
import com.example.datastore.di.dataStoreModule
import com.example.gamification.di.gamificationModule
import com.example.habit.di.habitModule
import com.example.network.di.networkModule
import com.example.notification.di.notificationModule
import com.example.profile.di.profileModule
import com.example.task.di.taskModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.annotation.KoinApplication
import org.koin.core.context.startKoin
import timber.log.Timber

@KoinApplication
class BloomApplication : Application() {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BloomApplication)
            androidLogger()
            modules(
                dataModule,
                databaseModule,
                dataStoreModule,
                notificationModule,
                gamificationModule,
                networkModule,
                authModule,
                taskModule,
                habitModule,
                profileModule,
                appModule,
            )
        }

        createNotificationChannel(applicationContext)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
