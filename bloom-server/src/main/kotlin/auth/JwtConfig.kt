package com.example.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.github.cdimascio.dotenv.dotenv

object JwtConfig {
    private val dotenv = dotenv()

    private val jwtSecret = dotenv["JWT_SECRET"]
    private val jwtIssuer = dotenv["JWT_ISSUER"]
    private val jwtAudience = dotenv["JWT_AUDIENCE"]

    private val algorithm = Algorithm.HMAC256(jwtSecret)

    val verifier: JWTVerifier =
        JWT
            .require(algorithm)
            .withAudience(jwtAudience)
            .withIssuer(jwtIssuer)
            .build()

    fun generateToken(userId: String): String =
        JWT
            .create()
            .withAudience(jwtAudience)
            .withIssuer(jwtIssuer)
            .withClaim("userId", userId)
            .sign(algorithm)
}
