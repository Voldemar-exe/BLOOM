package db.tables

import com.example.db.tables.UsersTable
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object UserCustomizationsTable : LongIdTable("user_customizations") {
    val userId = reference("user_id", UsersTable)
    val key = text("key")
    val type = varchar("type", 50)
}
