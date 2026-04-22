package com.example.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.database.dao.SubtaskDao
import com.example.database.dao.TaskDao
import com.example.database.dao.TaskReminderDao
import com.example.database.dao.TaskWithRelationDao
import com.example.database.model.Habit
import com.example.database.model.HabitPlant
import com.example.database.model.HabitReminder
import com.example.database.model.SubtaskEntity
import com.example.database.model.TaskEntity
import com.example.database.model.TaskReminderEntity
import com.example.database.util.InstantConverter
import com.example.database.util.IntListConverter
import com.example.database.util.StringListConverter

@Database(
    entities = [
        Habit::class,
        HabitPlant::class,
        HabitReminder::class,
        SubtaskEntity::class,
        TaskEntity::class,
        TaskReminderEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(
    value = [
        InstantConverter::class,
        IntListConverter::class,
        StringListConverter::class,
    ],
)
internal abstract class BloomDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    abstract fun subtaskDao(): SubtaskDao

    abstract fun taskReminderDao(): TaskReminderDao

    abstract fun taskWithRelationDao(): TaskWithRelationDao
}
