package com.example.bloom

import android.app.Application
import com.example.data.di.dataModule
import com.example.database.di.databaseModule
import com.example.datastore.di.dataStoreModule
import com.example.habit.di.habitModule
import com.example.profile.di.profileModule
import com.example.task.di.taskModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.annotation.KoinApplication
import org.koin.core.context.startKoin
import timber.log.Timber

@KoinApplication
class BloomApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@BloomApplication)
            modules(
                dataModule,
                databaseModule,
                taskModule,
                habitModule,
                profileModule,
                dataStoreModule,
            )
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
