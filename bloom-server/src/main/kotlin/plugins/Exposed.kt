package com.example.plugins

import com.example.auth.ExposedUser
import com.example.auth.ExposedUserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase

suspend fun Application.configureExposed() {
    val database = R2dbcDatabase.connect(
        url = "r2dbc:h2:file:///./h2",
        user = "root",
        password = "",
    )
    val userService = ExposedUserService(database).also {
        it.createSchema()
    }

    routing {
        // Create user
        post("/users") {
            val user = call.receive<ExposedUser>()
            val id = userService.create(user)
            call.respond(HttpStatusCode.Created, id)
        }

        // Read user
        get("/users/{id}") {
            val id = call.parameters["id"]?.toUInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = userService.read(id)
            if (user != null) {
                call.respond(HttpStatusCode.OK, user)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        // Update user
        put("/users/{id}") {
            val id = call.parameters["id"]?.toUInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = call.receive<ExposedUser>()
            userService.update(id, user)
            call.respond(HttpStatusCode.NoContent)
        }

        // Delete user
        delete("/users/{id}") {
            val id = call.parameters["id"]?.toUInt() ?: throw IllegalArgumentException("Invalid ID")
            userService.delete(id)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
