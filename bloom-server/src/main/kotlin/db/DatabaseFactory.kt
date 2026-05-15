package com.example.db

import com.example.db.tables.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import db.tables.UserAchievementsTable
import db.tables.UserCustomizationsTable
import io.ktor.server.config.*
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

object DatabaseFactory {
    fun init(config: ApplicationConfig) {
        val dbConfig = config.config("database")
        val host = dbConfig.property("host").getString()
        val port = dbConfig.property("port").getString().toInt()
        val name = dbConfig.property("name").getString()
        val user = dbConfig.property("user").getString()
        val password = dbConfig.property("password").getString()
        val ssl = dbConfig.propertyOrNull("ssl")?.getString()?.toBoolean() ?: false
        val poolSize = dbConfig.propertyOrNull("poolSize")?.getString()?.toIntOrNull() ?: 10

        val jdbcUrl =
            "jdbc:postgresql://$host:$port/$name?sslmode=${if (ssl) "require" else "disable"}"

        val hikariConfig =
            HikariConfig().apply {
                this.jdbcUrl = jdbcUrl
                driverClassName = "org.postgresql.Driver"
                this.username = user
                this.password = password
                maximumPoolSize = poolSize
                connectionTimeout = 30000
                maxLifetime = 1800000
                validate()
            }

        Database.connect(
            HikariDataSource(hikariConfig),
        )
    }

    suspend fun createTables() {
        suspendTransaction {
            SchemaUtils.create(
                UsersTable,
                UserStatsTable,
                AppSettingsTable,
                UserCustomizationsTable,
                UserAchievementsTable,
                HabitsTable,
                HabitPlantsTable,
                HabitRemindersTable,
                TasksTable,
                SubtasksTable,
                TaskRemindersTable,
                StatsLogsTable,
                HabitCompletionsTable,
                TaskCompletionsTable,
            )
        }
    }
}
