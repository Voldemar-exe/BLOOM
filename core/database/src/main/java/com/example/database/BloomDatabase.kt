package com.example.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
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
import com.example.database.model.entities.HabitCompletionEntity
import com.example.database.model.entities.HabitEntity
import com.example.database.model.entities.HabitPlantEntity
import com.example.database.model.entities.HabitReminderEntity
import com.example.database.model.entities.StatsLogEntity
import com.example.database.model.entities.SubtaskEntity
import com.example.database.model.entities.SyncQueueEntity
import com.example.database.model.entities.TaskCompletionEntity
import com.example.database.model.entities.TaskEntity
import com.example.database.model.entities.TaskReminderEntity
import com.example.database.util.IntListConverter
import com.example.database.util.RecurrenceConverter
import com.example.database.util.StringListConverter

@Database(
    entities = [
        HabitEntity::class,
        HabitPlantEntity::class,
        HabitReminderEntity::class,
        SubtaskEntity::class,
        TaskEntity::class,
        TaskReminderEntity::class,
        StatsLogEntity::class,
        HabitCompletionEntity::class,
        TaskCompletionEntity::class,
        SyncQueueEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(
    value = [
        IntListConverter::class,
        StringListConverter::class,
        RecurrenceConverter::class,
    ],
)
internal abstract class BloomDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    abstract fun subtaskDao(): SubtaskDao

    abstract fun taskReminderDao(): TaskReminderDao

    abstract fun taskWithRelationDao(): TaskWithRelationDao

    abstract fun habitDao(): HabitDao

    abstract fun habitPlantDao(): HabitPlantDao

    abstract fun habitReminderDao(): HabitReminderDao

    abstract fun habitWithRelationDao(): HabitWithRelationDao

    abstract fun gamificationDao(): GamificationDao

    abstract fun syncQueueDao(): SyncQueueDao

    abstract fun syncDao(): SyncDao

    abstract fun statsDao(): StatsDao
}
