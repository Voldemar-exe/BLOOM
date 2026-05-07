package com.example.db

import io.ktor.server.config.*
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import io.r2dbc.spi.IsolationLevel
import io.r2dbc.spi.Option
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabaseConfig

object DatabaseFactory {
    private lateinit var connectionFactory: ConnectionFactory

    fun init(config: ApplicationConfig) {
        val dbConfig = config.config("database")

        val options = ConnectionFactoryOptions.builder()
            .option(ConnectionFactoryOptions.DRIVER, "postgresql")
            .option(ConnectionFactoryOptions.HOST, dbConfig.property("host").getString())
            .option(ConnectionFactoryOptions.PORT, dbConfig.property("port").getString().toInt())
            .option(ConnectionFactoryOptions.DATABASE, dbConfig.property("database").getString())
            .option(ConnectionFactoryOptions.USER, dbConfig.property("user").getString())
            .option(ConnectionFactoryOptions.PASSWORD, dbConfig.property("password").getString())
            .option(
                ConnectionFactoryOptions.SSL,
                dbConfig.propertyOrNull("ssl")?.getString()?.toBoolean() ?: false
            )
            .apply {
                val poolSize = dbConfig.propertyOrNull("poolSize")?.getString()?.toIntOrNull() ?: 10
                option(Option.valueOf("option"), "MAX_CONNECTIONS=$poolSize")
            }
            .build()

        val databaseConfig = R2dbcDatabaseConfig {
            defaultMaxAttempts = 1
            defaultR2dbcIsolationLevel = IsolationLevel.READ_UNCOMMITTED
            connectionFactoryOptions = options
        }

        R2dbcDatabase.connect(databaseConfig = databaseConfig)
    }
}