package com.example.data

import com.example.data.repository.SettingsRepositoryImpl
import com.example.datastore.datastore.BloomPreferencesDataStore
import com.example.model.AppSettings
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SettingsRepositoryImplTest {
    private val dataStore = mockk<BloomPreferencesDataStore>()
    private lateinit var repository: SettingsRepositoryImpl

    @Before
    fun setUp() {
        repository = SettingsRepositoryImpl(dataStore)
    }

    @Test
    fun `appearance flow maps theme from settings`() =
        runTest {
            val settings = mockk<AppSettings> { every { theme } returns "DARK" }
            every { dataStore.settings } returns flowOf(settings)
            Assert.assertEquals("DARK", repository.appearance.first().theme)
        }

    @Test
    fun `preferences flow maps goals`() =
        runTest {
            val settings =
                mockk<AppSettings> {
                    every { weeklyGoal } returns 5
                    every { streakTarget } returns 10
                }
            every { dataStore.settings } returns flowOf(settings)
            Assert.assertEquals(5, repository.preferences.first().weeklyGoal)
        }

    @Test
    fun `notifications flow maps flags`() =
        runTest {
            val settings =
                mockk<AppSettings> {
                    every { emailEnabled } returns true
                    every { pushEnabled } returns
                        false
                }
            every { dataStore.settings } returns flowOf(settings)
            val res = repository.notifications.first()
            Assert.assertTrue(res.emailEnabled)
            Assert.assertFalse(res.pushEnabled)
        }

    @Test
    fun `updateSettings delegates to dataStore`() =
        runTest {
            val s = mockk<AppSettings>()
            coEvery { dataStore.setSettings(s) } returns Unit
            repository.updateSettings(s)
            coVerify { dataStore.setSettings(s) }
        }
}
