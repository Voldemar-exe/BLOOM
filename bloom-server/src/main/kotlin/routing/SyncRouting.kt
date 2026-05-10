package com.example.routing

import com.example.model.SyncPushRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import sync.SyncService

fun Application.configureSyncRouting() {
    val syncService: SyncService by inject<SyncService>()

    routing {
        authenticate("jwt") {
            route("/sync") {
                post("/push") {
                    val principal =
                        call.principal<JWTPrincipal>() ?: run {
                            call.respond(HttpStatusCode.Unauthorized)
                            return@post
                        }
                    val userId = principal.payload.getClaim("userId").asLong()
                    val request = call.receive<SyncPushRequest>()

                    syncService
                        .push(userId, request)
                        .onSuccess { call.respond(HttpStatusCode.OK) }
                        .onFailure {
                            call.respond(
                                HttpStatusCode.InternalServerError,
                                it.message ?: "Sync failed",
                            )
                        }
                }

                post("/pull") {
                    val principal =
                        call.principal<JWTPrincipal>() ?: run {
                            call.respond(HttpStatusCode.Unauthorized)
                            return@post
                        }
                    val userId = principal.payload.getClaim("userId").asLong()
                    val lastSync = call.request.queryParameters["lastSync"]?.toLongOrNull() ?: 0L

                    val response = syncService.pull(userId, lastSync)
                    call.respond(HttpStatusCode.OK, response)
                }
            }
        }
    }
}
