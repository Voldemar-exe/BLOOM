package com.example.db.daos

import com.example.db.tables.UsersTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass

class UserDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserDAO>(UsersTable)

    var email by UsersTable.email
    var passwordHash by UsersTable.passwordHash
    var createdAt by UsersTable.createdAt
    var updatedAt by UsersTable.updatedAt
}