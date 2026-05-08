package com.example.auth

import com.example.db.daos.UserDAO
import com.example.db.tables.UsersTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

interface AuthService {
    suspend fun register(
        login: String,
        email: String,
        password: String,
    ): Result<String>

    suspend fun login(
        login: String,
        password: String,
    ): Result<String>
}

class AuthServiceImpl : AuthService {
    override suspend fun register(
        login: String,
        email: String,
        password: String,
    ): Result<String> =
        suspendTransaction {
            try {
                if (UserDAO.find { UsersTable.login eq login }.firstOrNull() != null) {
                    return@suspendTransaction Result.failure(AuthError.LoginAlreadyExists())
                }
                if (UserDAO.find { UsersTable.email eq email }.firstOrNull() != null) {
                    return@suspendTransaction Result.failure(AuthError.EmailAlreadyExists())
                }

                val user =
                    UserDAO.new {
                        this.login = login
                        this.email = email
                        this.passwordHash = PasswordHasher.hash(password)
                        this.nickname = login
                        this.createdAt = System.currentTimeMillis()
                        this.updatedAt = System.currentTimeMillis()
                    }

                Result.success(JwtConfig.generateToken(user.id.value.toString()))
            } catch (e: Exception) {
                Result.failure(AuthError.InternalError(e))
            }
        }

    override suspend fun login(
        login: String,
        password: String,
    ): Result<String> =
        suspendTransaction {
            try {
                val user =
                    UserDAO.find { UsersTable.login eq login }.firstOrNull()
                        ?: return@suspendTransaction Result.failure(
                            AuthError.InvalidCredentials(),
                        )

                if (!PasswordHasher.verify(password, user.passwordHash)) {
                    return@suspendTransaction Result.failure(AuthError.InvalidCredentials())
                }

                Result.success(JwtConfig.generateToken(user.id.value.toString()))
            } catch (e: Exception) {
                Result.failure(AuthError.InternalError(e))
            }
        }
}

sealed class AuthError : Exception() {
    class LoginAlreadyExists : AuthError()

    class EmailAlreadyExists : AuthError()

    class InvalidCredentials : AuthError()

    data class InternalError(override val cause: Throwable) : AuthError()
}
