package com.example.database.di

import androidx.room.Room
import com.example.database.BloomDatabase
import com.example.database.dao.GamificationDao
import com.example.database.dao.HabitDao
import com.example.database.dao.HabitPlantDao
import com.example.database.dao.HabitReminderDao
import com.example.database.dao.HabitWithRelationDao
import com.example.database.dao.StatsDao
import com.example.database.dao.SubtaskDao
import com.example.database.dao.SyncDao
import com.example.database.dao.SyncQueueDao
import com.example.database.dao.TaskDao
import com.example.database.dao.TaskReminderDao
import com.example.database.dao.TaskWithRelationDao
import com.example.database.util.RoomTransactionRunner
import com.example.database.util.SyncTracker
import com.example.database.util.TransactionRunner
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
        single<TaskDao> { get<BloomDatabase>().taskDao() }
        single<SubtaskDao> { get<BloomDatabase>().subtaskDao() }
        single<TaskReminderDao> { get<BloomDatabase>().taskReminderDao() }
        single<TaskWithRelationDao> { get<BloomDatabase>().taskWithRelationDao() }
        single<HabitDao> { get<BloomDatabase>().habitDao() }
        single<HabitPlantDao> { get<BloomDatabase>().habitPlantDao() }
        single<HabitReminderDao> { get<BloomDatabase>().habitReminderDao() }
        single<HabitWithRelationDao> { get<BloomDatabase>().habitWithRelationDao() }
        single<GamificationDao> { get<BloomDatabase>().gamificationDao() }
        single<SyncQueueDao> { get<BloomDatabase>().syncQueueDao() }
        single<SyncDao> { get<BloomDatabase>().syncDao() }
        single<StatsDao> { get<BloomDatabase>().statsDao() }
        single<SyncTracker> { SyncTracker(get<SyncQueueDao>()) }
        single<TransactionRunner> { RoomTransactionRunner(get<BloomDatabase>()) }
    }
