package com.example

import com.example.auth.configureSecurity
import com.example.db.DatabaseFactory
import com.example.plugins.configureHttp
import com.example.plugins.configureSerialization
import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.*
import io.ktor.server.netty.*
import kotlinx.coroutines.launch
import org.koin.core.context.startKoin

fun main(args: Array<String>) {

    val dotenv = dotenv {
        directory = "."
        filename = ".env"
        ignoreIfMissing = true
    }

    dotenv.entries().forEach { entry ->
        System.setProperty(entry.key, entry.value)
    }

    EngineMain.main(args)
}

fun Application.module() {

    startKoin {
//        modules(authModule)
    }

    DatabaseFactory.init(environment.config)

    launch {
        DatabaseFactory.createTables()
    }

    configureSerialization()
    configureHttp()
    configureSecurity()
//    configureMonitoring()
//    configureFrameworks()
//    configureAuthRouting()
//    configureDataRouting()
}
