package db.daos

import db.tables.UserAchievementsTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass

class UserAchievementDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserAchievementDAO>(UserAchievementsTable)

    var userId by UserAchievementsTable.userId
    var achievementId by UserAchievementsTable.achievementId
}
