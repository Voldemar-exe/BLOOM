package com.example.model

sealed class AuthError : Exception() {
    abstract val code: String
    abstract val httpStatusCode: io.ktor.http.HttpStatusCode
    abstract val userMessage: String

    class LoginAlreadyExists : AuthError() {
        override val code = "LOGIN_ALREADY_EXISTS"
        override val httpStatusCode = io.ktor.http.HttpStatusCode.Conflict
        override val userMessage = "Пользователь с таким логином уже существует"
    }

    class EmailAlreadyExists : AuthError() {
        override val code = "EMAIL_ALREADY_EXISTS"
        override val httpStatusCode = io.ktor.http.HttpStatusCode.Conflict
        override val userMessage = "Пользователь с таким email уже существует"
    }

    class InvalidCredentials : AuthError() {
        override val code = "INVALID_CREDENTIALS"
        override val httpStatusCode = io.ktor.http.HttpStatusCode.Unauthorized
        override val userMessage = "Неверный логин или пароль"
    }

    data class InternalError(override val cause: Throwable) : AuthError() {
        override val code = "INTERNAL_ERROR"
        override val httpStatusCode = io.ktor.http.HttpStatusCode.InternalServerError
        override val userMessage = "Внутренняя ошибка сервера"
    }
}