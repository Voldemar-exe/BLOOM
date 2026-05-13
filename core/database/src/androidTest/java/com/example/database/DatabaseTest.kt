package com.example.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.database.dao.GamificationDao
import com.example.database.dao.HabitDao
import com.example.database.dao.HabitPlantDao
import com.example.database.dao.HabitReminderDao
import com.example.database.dao.HabitWithRelationDao
import com.example.database.dao.SubtaskDao
import com.example.database.dao.SyncDao
import com.example.database.dao.SyncQueueDao
import com.example.database.dao.TaskDao
import com.example.database.dao.TaskReminderDao
import com.example.database.dao.TaskWithRelationDao
import com.example.database.model.entities.HabitEntity
import com.example.database.model.entities.HabitPlantEntity
import com.example.database.model.entities.HabitReminderEntity
import com.example.database.model.entities.SubtaskEntity
import com.example.database.model.entities.TaskEntity
import com.example.database.model.entities.TaskReminderEntity
import com.example.database.util.SyncTracker
import com.example.model.Priority
import com.example.model.Recurrence
import com.example.model.RecurrenceType
import org.junit.After
import org.junit.Before

internal abstract class DatabaseTest {
    protected lateinit var db: BloomDatabase

    protected lateinit var gamificationDao: GamificationDao
    protected lateinit var habitDao: HabitDao
    protected lateinit var habitPlantDao: HabitPlantDao
    protected lateinit var habitReminderDao: HabitReminderDao
    protected lateinit var habitWithRelationDao: HabitWithRelationDao
    protected lateinit var subtaskDao: SubtaskDao
    protected lateinit var syncDao: SyncDao
    protected lateinit var syncQueueDao: SyncQueueDao
    protected lateinit var taskDao: TaskDao
    protected lateinit var taskReminderDao: TaskReminderDao
    protected lateinit var taskWithRelationDao: TaskWithRelationDao
    protected lateinit var tracker: SyncTracker

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        db =
            Room
                .inMemoryDatabaseBuilder(
                    context,
                    BloomDatabase::class.java,
                ).allowMainThreadQueries()
                .build()

        gamificationDao = db.gamificationDao()
        habitDao = db.habitDao()
        habitPlantDao = db.habitPlantDao()
        habitReminderDao = db.habitReminderDao()
        habitWithRelationDao = db.habitWithRelationDao()
        subtaskDao = db.subtaskDao()
        syncDao = db.syncDao()
        syncQueueDao = db.syncQueueDao()
        taskDao = db.taskDao()
        taskReminderDao = db.taskReminderDao()
        taskWithRelationDao = db.taskWithRelationDao()

        tracker = SyncTracker(syncQueueDao)
    }

    @After
    fun teardown() {
        db.close()
    }

    protected fun habit(
        id: Long = 1L,
        title: String = "Habit $id",
    ) = HabitEntity(
        id = id,
        title = title,
        description = "description",
        recurrence = Recurrence(RecurrenceType.DAY),
        tags = listOf("health"),
        steps = listOf("step1"),
    )

    protected fun task(
        id: Long = 1L,
        title: String = "Task $id",
    ) = TaskEntity(
        id = id,
        title = title,
        description = "description",
        recurrence = Recurrence(RecurrenceType.DAY),
        priority = Priority.MEDIUM,
        deadline = null,
        tags = listOf("work"),
        isArchived = false,
        isPaused = false,
        isMuted = false,
    )

    protected fun subtask(
        id: Long = 1L,
        taskId: Long = 1L,
        isChecked: Boolean = false,
    ) = SubtaskEntity(
        id = id,
        taskId = taskId,
        title = "Subtask $id",
        isChecked = isChecked,
    )

    protected fun habitReminder(
        id: Long = 1L,
        habitId: Long = 1L,
    ) = HabitReminderEntity(
        id = id,
        habitId = habitId,
        reminderTime = "10:00",
        isEnabled = true,
    )

    protected fun taskReminder(
        id: Long = 1L,
        taskId: Long = 1L,
    ) = TaskReminderEntity(
        id = id,
        taskId = taskId,
        reminderTime = "12:00",
        isEnabled = true,
    )

    protected fun plant(
        id: Long = 1L,
        habitId: Long = 1L,
    ) = HabitPlantEntity(
        id = id,
        habitId = habitId,
        presetId = 1,
        iterations = 5,
        variability = 0.4f,
        seed = 123L,
        baseAngle = 20f,
        baseLength = 50f,
        baseWidth = 8f,
        widthFalloff = 0.7f,
        widthFalloffEndAt = 0.8f,
        petalLength = 12f,
        petalType = "round",
        petalColor = 0xFFFFFF,
        baseColor = 0x000000,
        petalAlpha = 0.8f,
    )
}

