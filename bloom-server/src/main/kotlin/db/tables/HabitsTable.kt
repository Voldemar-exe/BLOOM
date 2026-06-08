package com.example.db.tables

import com.example.model.SyncStatus
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object HabitsTable : LongIdTable("habits") {
    val localId = long("local_id").index()
    val userId = reference("user_id", UsersTable.id).index()
    val title = text("title")
    val description = text("description").nullable()
    val recurrence = text("recurrence")
    val tags = array<String>("tags").nullable()
    val steps = array<String>("steps").nullable()
    val isChecked = bool("is_checked").default(false)
    val isArchived = bool("is_archived").default(false)
    val isPaused = bool("is_paused").default(false)
    val isMuted = bool("is_muted").default(false)
    val startAt = long("start_at")
    val endAt = long("end_at").nullable()
    val createdAt = long("created_at").index()
    val updatedAt = long("updated_at")
    val syncStatus = enumeration<SyncStatus>("sync_status").default(SyncStatus.SYNCED)
}
