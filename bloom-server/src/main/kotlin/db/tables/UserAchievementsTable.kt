package db.tables

import com.example.db.tables.UsersTable
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object UserAchievementsTable : LongIdTable("user_achievements") {
    val userId = reference("user_id", UsersTable)
    val achievementId = integer("achievement_id")
}
