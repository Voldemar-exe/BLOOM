package com.example.database.di

import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.example.database.BloomDatabase
import com.example.database.dao.SubtaskDao
import com.example.database.dao.TaskDao
import com.example.database.dao.TaskReminderDao
import com.example.database.dao.TaskWithRelationDao
import org.koin.core.annotation.KoinViewModel
import org.koin.dsl.module

val databaseModule =
    module {
        single<BloomDatabase> {
            Room
                .databaseBuilder(
                    get(),
                    BloomDatabase::class.java,
                    "bloom-database",
                ).build()
        }
        single<TaskDao> {
            get<BloomDatabase>().taskDao()
        }
        single<SubtaskDao> {
            get<BloomDatabase>().subtaskDao()
        }
        single<TaskReminderDao> {
            get<BloomDatabase>().taskReminderDao()
        }
        single<TaskWithRelationDao> {
            get<BloomDatabase>().taskWithRelationDao()
        }
    }

@KoinViewModel
class DBViewModel : ViewModel()
