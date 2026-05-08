package com.example

import com.example.auth.configureSecurity
import com.example.db.DatabaseFactory
import com.example.plugins.configureHttp
import com.example.plugins.configureKoin
import com.example.plugins.configureSerialization
import com.example.routing.configureAuthRouting
import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.launch

fun main(args: Array<String>) {
    val dotenv =
        dotenv {
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
    configureKoin()

    DatabaseFactory.init(environment.config)

    launch {
        DatabaseFactory.createTables()
    }

    routing {
        get("/") {
            call.respondText("Hello, World!")
        }
    }

    configureSerialization()
    configureHttp()
    configureSecurity()
//    configureMonitoring()
//    configureFrameworks()
    configureAuthRouting()
//    configureDataRouting()
}
