package com.example

import com.example.auth.AuthService
import com.example.auth.AuthServiceImpl
import com.example.model.AuthError
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import kotlin.test.Test

class AuthServiceIntegrationTest {
    private lateinit var service: AuthService

    @Before
    fun setup() {
        TestDatabaseFactory.init()
        service = AuthServiceImpl()
    }

    @After
    fun tearDown() {
        TestDatabaseFactory.clear()
    }

    @Test
    fun register_createsUser() =
        runTest {
            val result =
                service.register(
                    login = "testUser",
                    email = "test@mail.com",
                    password = "123456",
                )

            Assert.assertTrue(result.isSuccess)
            Assert.assertNotNull(result.getOrNull())
        }

    @Test
    fun register_fails_whenLoginAlreadyExists() =
        runTest {
            service.register(
                login = "testUser",
                email = "first@mail.com",
                password = "123456",
            )

            val result =
                service.register(
                    login = "testUser",
                    email = "second@mail.com",
                    password = "123456",
                )

            Assert.assertTrue(result.isFailure)
            Assert.assertTrue(result.exceptionOrNull() is AuthError.LoginAlreadyExists)
        }

    @Test
    fun login_returnsToken_whenCredentialsValid() =
        runTest {
            service.register(
                login = "testUser",
                email = "test@mail.com",
                password = "123456",
            )

            val result =
                service.login(
                    login = "testUser",
                    password = "123456",
                )

            Assert.assertTrue(result.isSuccess)
            Assert.assertNotNull(result.getOrNull())
        }

    @Test
    fun login_fails_whenPasswordIncorrect() =
        runTest {
            service.register(
                login = "testUser",
                email = "test@mail.com",
                password = "123456",
            )

            val result =
                service.login(
                    login = "testUser",
                    password = "wrong",
                )

            Assert.assertTrue(result.isFailure)
            Assert.assertTrue(result.exceptionOrNull() is AuthError.InvalidCredentials)
        }
}