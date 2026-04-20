package com.example.auth

sealed interface AuthAction {
    data class LoginChanged(
        val login: String,
    ) : AuthAction

    data class EmailChanged(
        val email: String,
    ) : AuthAction

    data class PasswordChanged(
        val password: String,
    ) : AuthAction

    data class ConfirmPasswordChanged(
        val confirmPassword: String,
    ) : AuthAction

    data class LoginClicked(
        val login: String,
        val password: String,
    ) : AuthAction

    data class RegisterClicked(
        val login: String?,
        val email: String,
        val password: String,
    ) : AuthAction

    object ToggleAuthMode : AuthAction

    object SkipAuth : AuthAction
}
