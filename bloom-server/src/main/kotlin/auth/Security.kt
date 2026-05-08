package com.example.auth

import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity() {
    val dotenv = dotenv()

    val jwtAudience = dotenv["JWT_AUDIENCE"]
    val jwtRealm = dotenv["JWT_REALM"]

    authentication {
        jwt {
            realm = jwtRealm

            verifier(JwtConfig.verifier)

            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}
