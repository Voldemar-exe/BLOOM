package com.example.data

import com.example.data.repository.AuthRepository
import com.example.data.repository.UserRepositoryImpl
import com.example.data.util.UserGarbageCollector
import com.example.datastore.datastore.BloomPreferencesDataStore
import com.example.model.CustomizationType
import com.example.model.User
import com.example.model.UserStats
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class UserRepositoryImplTest {
    private val dataStore = mockk<BloomPreferencesDataStore>(relaxed = true)
    private val authRepository = mockk<AuthRepository>(relaxed = true)
    private val garbageCollector = mockk<UserGarbageCollector>(relaxed = true)
    private lateinit var repository: UserRepositoryImpl

    @Before
    fun setUp() {
        repository = UserRepositoryImpl(dataStore, authRepository, garbageCollector)
    }

    @Test
    fun `user flows from dataSource`() =
        runTest {
            val u = mockk<User>(relaxed = true)
            every { dataStore.user } returns flowOf(u)
            repository = UserRepositoryImpl(dataStore, authRepository, garbageCollector)
            assertEquals(u, repository.user.first())
        }

    @Test
    fun `updateUser delegates`() =
        runTest {
            val u = mockk<User>(relaxed = true)
            repository.updateUser(u)
            coVerify { dataStore.setUser(u) }
        }

    @Test
    fun `updateStats delegates`() =
        runTest {
            val s = mockk<UserStats>(relaxed = true)
            repository.updateStats(s)
            coVerify { dataStore.setStats(s) }
        }

    @Test
    fun `updateUsername copies current user`() =
        runTest {
            val current =
                mockk<User>(relaxed = true) { every { username } returns "old" }
            every { dataStore.user } returns flowOf(current)
            repository = UserRepositoryImpl(dataStore, authRepository, garbageCollector)
            repository.updateUsername("new")
            coVerify { dataStore.setUser(any()) }
        }

    @Test
    fun `updateUsername does nothing if null`() =
        runTest {
            every { dataStore.user } returns flowOf(null)
            repository = UserRepositoryImpl(dataStore, authRepository, garbageCollector)
            repository.updateUsername("new")
            coVerify(exactly = 0) { dataStore.setUser(any()) }
        }

    @Test
    fun `updateCustomization avatar updates key`() =
        runTest {
            val current =
                mockk<User>(relaxed = true) { every { avatarKey } returns "old" }
            every { dataStore.user } returns flowOf(current)
            repository = UserRepositoryImpl(dataStore, authRepository, garbageCollector)
            repository.updateCustomization("newAv", CustomizationType.AVATAR)
            coVerify { dataStore.setUser(any()) }
        }

    @Test
    fun `addAchievement delegates`() =
        runTest {
            repository.addAchievement(5)
            coVerify { dataStore.addAchievement(5) }
        }

    @Test
    fun `addPurchase deducts coins and adds item`() =
        runTest {
            val u =
                mockk<User>(relaxed = true) {
                    every { ownedItems } returns
                        emptyList()
                }
            val s =
                mockk<UserStats>(relaxed = true) {
                    every { currentCoinsAmount } returns
                        100
                }
            every { dataStore.user } returns flowOf(u)
            every { dataStore.stats } returns flowOf(s)
            repository = UserRepositoryImpl(dataStore, authRepository, garbageCollector)
            repository.addPurchase("item", CustomizationType.AVATAR, 20)
            coVerify { dataStore.addPurchase("item", "AVATAR") }
            coVerify { dataStore.setStats(any()) }
            coVerify { dataStore.setUser(any()) }
        }

    @Test
    fun `clearAll calls garbageCollector`() =
        runTest {
            repository.clearAll()
            coVerify { garbageCollector.clearAll() }
        }
}
