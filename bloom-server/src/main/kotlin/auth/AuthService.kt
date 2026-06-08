package com.example.auth

import com.example.db.daos.AppSettingsDAO
import com.example.db.daos.UserDAO
import com.example.db.daos.UserStatsDAO
import com.example.db.tables.UsersTable
import com.example.model.AuthError
import db.daos.UserCustomizationDAO
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

interface AuthService {
    suspend fun register(
        login: String,
        email: String,
        password: String,
    ): Result<String>

    suspend fun login(
        login: String,
        password: String,
    ): Result<String>
}

class AuthServiceImpl : AuthService {
    override suspend fun register(
        login: String,
        email: String,
        password: String,
    ): Result<String> =
        suspendTransaction {
            try {
                if (UserDAO.find { UsersTable.login eq login }.firstOrNull() != null) {
                    return@suspendTransaction Result.failure(AuthError.LoginAlreadyExists())
                }
                if (UserDAO.find { UsersTable.email eq email }.firstOrNull() != null) {
                    return@suspendTransaction Result.failure(AuthError.EmailAlreadyExists())
                }

                val user =
                    UserDAO.new {
                        this.login = login
                        this.email = email
                        this.passwordHash = PasswordHasher.hash(password)
                        this.nickname = login
                        this.createdAt = System.currentTimeMillis()
                        this.updatedAt = System.currentTimeMillis()
                    }

                createDefaultUserStats(user.id)
                createDefaultAppSettings(user.id)
                createDefaultCustomizations(user.id)

                Result.success(JwtConfig.generateToken(user.id.value.toString()))
            } catch (e: Exception) {
                Result.failure(AuthError.InternalError(e))
            }
        }

    override suspend fun login(
        login: String,
        password: String,
    ): Result<String> =
        suspendTransaction {
            try {
                val user =
                    UserDAO.find { UsersTable.login eq login }.firstOrNull()
                        ?: return@suspendTransaction Result.failure(
                            AuthError.InvalidCredentials(),
                        )

                if (!PasswordHasher.verify(password, user.passwordHash)) {
                    return@suspendTransaction Result.failure(AuthError.InvalidCredentials())
                }

                Result.success(JwtConfig.generateToken(user.id.value.toString()))
            } catch (e: Exception) {
                Result.failure(AuthError.InternalError(e))
            }
        }
}

private fun createDefaultUserStats(userId: EntityID<Long>) {
    UserStatsDAO.new {
        this.userId = userId
        level = 1
        currentExperience = 0
        currentCoinsAmount = 0
        maxCoinsAmount = 0
        totalHabitsCreated = 0
        totalHabitsCompleted = 0
        totalTasksCreated = 0
        totalTasksCompleted = 0
        currentStreak = 0
        longestStreak = 0
        updatedAt = System.currentTimeMillis()
    }
}

private fun createDefaultAppSettings(userId: EntityID<Long>) {
    AppSettingsDAO.new {
        this.userId = userId
        theme = "SYSTEM"
        weeklyGoal = 5
        streakTarget = 7
        emailEnabled = true
        pushEnabled = true
        habitRemindersEnabled = true
        taskRemindersEnabled = true
        updatedAt = System.currentTimeMillis()
    }
}

private fun createDefaultCustomizations(userId: EntityID<Long>) {
    val defaults =
        listOf(
            "JUST_GUY" to "AVATAR",
            "BLACK_SAND" to "BACKGROUND",
            "GREEN" to "COLOR",
        )

    defaults.forEach { (key, type) ->
        UserCustomizationDAO.new {
            this.userId = userId
            this.key = key
            this.type = type
        }
    }
}

suspend fun createDemoUser() {
    suspendTransaction {
        val login = "demo"

        if (UserDAO.find { UsersTable.login eq login }.firstOrNull() != null) {
            return@suspendTransaction
        }

        val user = UserDAO.new {
            this.login = login
            this.email = "demo@yandex.ru"
            this.passwordHash = PasswordHasher.hash("demo123")
            this.nickname = "Voldemar"
            this.createdAt = System.currentTimeMillis()
            this.updatedAt = System.currentTimeMillis()
        }

        createDemoStats(user.id)
        createDemoSettings(user.id)
        createDemoCustomizations(user.id)
        createDemoHabits(user.id)
        createDemoTasks(user.id)
    }
}

private fun createDemoStats(userId: EntityID<Long>) {
    UserStatsDAO.new {
        this.userId = userId

        level = 12
        currentExperience = 2350

        currentCoinsAmount = 480
        maxCoinsAmount = 1250

        totalHabitsCreated = 8
        totalHabitsCompleted = 143

        totalTasksCreated = 57
        totalTasksCompleted = 48

        currentStreak = 12
        longestStreak = 27

        updatedAt = System.currentTimeMillis()
    }
}

private fun createDemoSettings(userId: EntityID<Long>) {
    AppSettingsDAO.new {
        this.userId = userId

        theme = "DARK"
        weeklyGoal = 7
        streakTarget = 14

        emailEnabled = true
        pushEnabled = true

        habitRemindersEnabled = true
        taskRemindersEnabled = true

        updatedAt = System.currentTimeMillis()
    }
}

private fun createDemoCustomizations(userId: EntityID<Long>) {
    listOf(
        "FOCUSED_GUY" to "AVATAR",
        "DUBAI_DOWNTOWN" to "BACKGROUND",
        "CYAN" to "COLOR",
    ).forEach { (key, type) ->
        UserCustomizationDAO.new {
            this.userId = userId
            this.key = key
            this.type = type
        }
    }
}

private fun createDemoHabits(userId: EntityID<Long>) {

}

private fun createDemoTasks(userId: EntityID<Long>) {

}