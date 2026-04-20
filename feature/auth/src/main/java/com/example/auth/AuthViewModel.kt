package com.example.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
class AuthViewModel(
//    private val authRepository: AuthRepository
) : ViewModel() {
    private val _authUiState = MutableStateFlow(AuthState())
    val authUiState: StateFlow<AuthState>
        get() = _authUiState.asStateFlow()

    fun onAction(action: AuthAction) {
        when (action) {
            is AuthAction.LoginChanged -> {
                updateLogin(action.login)
            }

            is AuthAction.EmailChanged -> {
                updateEmail(action.email)
            }

            is AuthAction.PasswordChanged -> {
                updatePassword(action.password)
            }

            is AuthAction.ConfirmPasswordChanged -> {
                updateConfirmPassword(action.confirmPassword)
            }

            is AuthAction.LoginClicked -> {
                handleLogin(action.login, action.password)
            }

            is AuthAction.RegisterClicked -> {
                handleRegister(
                    action.login,
                    action.email,
                    action.password,
                )
            }

            is AuthAction.ToggleAuthMode -> {
                toggleAuthMode()
            }

            AuthAction.SkipAuth -> {
                navigateTo()
            }
        }
    }

    private fun updateEmail(email: String) {
        _authUiState.update { it.copy(email = email, errorMessage = null) }
    }

    private fun updatePassword(password: String) {
        _authUiState.update { it.copy(password = password, errorMessage = null) }
    }

    private fun updateConfirmPassword(password: String) {
        _authUiState.update { it.copy(password = password, errorMessage = null) }
    }

    private fun updateLogin(login: String) {
        _authUiState.update { it.copy(login = login, errorMessage = null) }
    }

    private fun handleLogin(
        login: String,
        password: String,
    ) {
        if (!validateInput(login, password)) return

        // TODO: Add action with AuthService
        viewModelScope.launch {
            Timber.d("Login successful")
        }
    }

    private fun handleRegister(
        login: String?,
        email: String,
        password: String,
    ) {
        if (!validateInput(email, password)) return

        viewModelScope.launch {
            _authUiState.update { it.copy(isLoading = true, errorMessage = null) }

            // TODO: Add action with AuthService
            viewModelScope.launch {
                Timber.d("Login successful")
            }
        }
    }

    private fun toggleAuthMode() {
        _authUiState.update {
            it.copy(
                isRegistrationMode = !it.isRegistrationMode,
                errorMessage = null,
            )
        }
    }

    private fun navigateTo() {
    }

    private fun validateInput(
        email: String,
        password: String,
    ): Boolean =
        when {
            email.isBlank() -> {
                _authUiState.update { it.copy(errorMessage = "Email cannot be empty") }
                false
            }

            password.isBlank() -> {
                _authUiState.update { it.copy(errorMessage = "Password cannot be empty") }
                false
            }

            password.length < 6 -> {
                _authUiState.update { it.copy(errorMessage = "Password must be at least 6 characters") }
                false
            }

            else -> {
                true
            }
        }
}
