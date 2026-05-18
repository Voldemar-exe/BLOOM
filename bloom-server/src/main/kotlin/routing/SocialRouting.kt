package com.example.routing

import com.example.sync.SocialService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import utils.deleteUser
import utils.userId

fun Application.configureSocialRouting() {
    val socialService: SocialService by inject<SocialService>()

    routing {
        authenticate("jwt") {
            get("/leaderboard") {
                val limit =
                    call.request.queryParameters["limit"]
                        ?.toIntOrNull()
                        ?: 100

                val result = socialService.getLeaderboard(limit)

                call.respond(result)
            }
            delete("/users/me") {
                val userId = call.userId()

                deleteUser(userId)

                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
