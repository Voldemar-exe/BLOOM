package com.example.db.daos

import com.example.db.tables.UsersTable
import com.example.model.UserProfileDto
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass

class UserDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserDAO>(UsersTable)

    var login by UsersTable.login
    var email by UsersTable.email
    var passwordHash by UsersTable.passwordHash
    var nickname by UsersTable.nickname
    var avatar by UsersTable.avatar
    var background by UsersTable.background
    var color by UsersTable.color
    var createdAt by UsersTable.createdAt
    var updatedAt by UsersTable.updatedAt
}

fun UserDAO.updateFrom(dto: UserProfileDto) {
    email = dto.email
    nickname = dto.username
    avatar = dto.avatarKey
    background = dto.backgroundKey
    color = dto.colorKey
}
