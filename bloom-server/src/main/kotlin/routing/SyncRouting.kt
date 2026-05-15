package com.example.routing

import com.example.model.SyncPushRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import sync.SyncService
import utils.userId

fun Application.configureSyncRouting() {
    val syncService: SyncService by inject<SyncService>()

    routing {
        authenticate("jwt") {
            route("/sync") {
                post("/push") {
                    val userId = call.userId()
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

                get("/pull") {
                    val userId = call.userId()
                    val lastSync = call.request.queryParameters["lastSync"]?.toLongOrNull() ?: 0L

                    val response = syncService.pull(userId, lastSync)
                    call.respond(HttpStatusCode.OK, response)
                }
            }
        }
    }
}
