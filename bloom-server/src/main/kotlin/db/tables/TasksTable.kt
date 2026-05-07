package com.example.db.tables

import com.example.model.SyncStatus
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object TasksTable : LongIdTable("tasks") {
    val userId = reference("user_id", UsersTable.id).index()
    val title = text("title")
    val description = text("description").nullable()
    val recurrence = text("recurrence")
    val priority = text("priority")
    val deadline = long("deadline").nullable()
    val tags = array<String>("tags").nullable()
    val isChecked = bool("is_checked").default(false)
    val isArchived = bool("is_archived").default(false)
    val isPaused = bool("is_paused").default(false)
    val isMuted = bool("is_muted").default(false)
    val createdAt = long("created_at")
    val updatedAt = long("updated_at").index()
    val syncStatus = enumeration<SyncStatus>("sync_status").default(SyncStatus.SYNCED)
}