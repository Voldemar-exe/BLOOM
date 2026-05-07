package com.example

import com.example.auth.configureSecurity
import com.example.plugins.configureHttp
import com.example.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.core.context.startKoin

fun main() {
    embeddedServer(
        Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {

    startKoin {
//        modules(authModule)
    }

//    DatabaseFactory.init(this)

    configureSerialization()
    configureHttp()
    configureSecurity()
//    configureMonitoring()
//    configureFrameworks()
//    configureAuthRouting()
//    configureDataRouting()
}
