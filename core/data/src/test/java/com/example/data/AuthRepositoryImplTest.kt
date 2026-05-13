package com.example.data

import com.example.data.repository.AuthRepositoryImpl
import com.example.datastore.datastore.BloomPreferencesDataStore
import com.example.network.api.AuthApi
import com.example.network.model.LoginResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AuthRepositoryImplTest {
    private val api = mockk<AuthApi>()
    private val dataStore = mockk<BloomPreferencesDataStore>(relaxed = true)
    private lateinit var repository: AuthRepositoryImpl

    @Before
    fun setUp() {
        every { dataStore.token } returns flowOf(null)
        repository = AuthRepositoryImpl(api, dataStore)
    }

    @Test
    fun `login success saves token`() =
        runTest {
            val response =
                mockk<LoginResponse>(relaxed = true) { every { token } returns "token123" }
            coEvery { api.login("user", "pass") } returns Result.success(response)
            val result = repository.login("user", "pass")
            assertTrue(result.isSuccess)
            coVerify { dataStore.saveToken("token123") }
        }

    @Test
    fun `login failure returns error`() =
        runTest {
            coEvery { api.login("user", "pass") } returns Result.failure(Exception("fail"))
            val result = repository.login("user", "pass")
            assertTrue(result.isFailure)
            coVerify(exactly = 0) { dataStore.saveToken(any()) }
        }

    @Test
    fun `register success saves token`() =
        runTest {
            val response =
                mockk<LoginResponse>(relaxed = true) { every { token } returns "regToken" }
            coEvery { api.register("user", "email@test", "pass") } returns Result.success(response)
            val result = repository.register("user", "email@test", "pass")
            assertTrue(result.isSuccess)
            coVerify { dataStore.saveToken("regToken") }
        }

    @Test
    fun `logout clears token`() =
        runTest {
            repository.logout()
            coVerify { dataStore.clearToken() }
        }

    @Test
    fun `skipAuth sets empty token and skipped flag`() =
        runTest {
            repository.skipAuth()
            coVerify { dataStore.saveToken(token = "", isSkipped = true) }
        }

    @Test
    fun `authToken flows from dataStore`() =
        runTest {
            coEvery { dataStore.token } returns flowOf("tok" to false)

            repository = AuthRepositoryImpl(api, dataStore)

            assertEquals("tok" to false, repository.authToken.first())
        }
}
