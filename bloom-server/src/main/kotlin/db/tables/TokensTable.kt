package com.example.db.tables

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object TokensTable : IntIdTable("tokens") {
    val userId = reference("id", UsersTable.id)
    val token = varchar("token", 255)
}