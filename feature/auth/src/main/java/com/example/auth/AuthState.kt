package com.example.auth

data class AuthState(
    val login: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRegistrationMode: Boolean = false,
)
