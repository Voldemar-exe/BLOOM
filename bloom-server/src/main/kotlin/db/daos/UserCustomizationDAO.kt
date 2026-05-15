package db.daos

import db.tables.UserCustomizationsTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass

class UserCustomizationDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserCustomizationDAO>(UserCustomizationsTable)

    var userId by UserCustomizationsTable.userId
    var key by UserCustomizationsTable.key
    var type by UserCustomizationsTable.type
}
