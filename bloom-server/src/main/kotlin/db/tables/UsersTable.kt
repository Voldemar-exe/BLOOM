package com.example.db.tables

import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object UsersTable : LongIdTable("users") {
    val login = varchar("login", 255).uniqueIndex()
    val email = varchar("email", 255).uniqueIndex()
    val passwordHash = varchar("password_hash", 255)
    val nickname = text("nickname")
    val avatar = text("avatar").default("JUST_GUY")
    val background = text("background").default("BLACK_SAND")
    val createdAt = long("created_at")
    val updatedAt = long("updated_at")
}
