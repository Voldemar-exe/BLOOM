package com.example.routing

import com.example.auth.AuthService
import com.example.model.AuthError
import com.example.model.LoginRequest
import com.example.model.LoginResponse
import com.example.model.RegisterRequest
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureAuthRouting() {
    val authService: AuthService by inject<AuthService>()

    routing {
        post("/login") {
            val request = call.receive<LoginRequest>()
            val result = authService.login(request.login, request.password)

            result.fold(
                onSuccess = { token ->
                    call.respond(LoginResponse(token))
                },
                onFailure = { error ->
                    val authError = error as? AuthError ?: AuthError.InternalError(error)
                    call.respond(
                        status = authError.httpStatusCode,
                        message = authError.message.toString(),
                    )
                },
            )
        }

        post("/register") {
            val request = call.receive<RegisterRequest>()
            val result = authService.register(request.login, request.email, request.password)

            result.fold(
                onSuccess = { token ->
                    call.respond(LoginResponse(token))
                },
                onFailure = { error ->
                    val authError = error as? AuthError ?: AuthError.InternalError(error)
                    call.respond(
                        status = authError.httpStatusCode,
                        message = authError.message.toString(),
                    )
                },
            )
        }
    }
}
