package com.example.data

import com.example.data.repository.ThemeRepositoryImpl
import com.example.datastore.datastore.BloomPreferencesDataStore
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
class ThemeRepositoryImplTest {
    private val dataStore = mockk<BloomPreferencesDataStore>(relaxed = true)
    private lateinit var repository: ThemeRepositoryImpl

    @Before
    fun setUp() {
        every { dataStore.theme } returns flowOf("SYSTEM")
        repository = ThemeRepositoryImpl(dataStore)
    }

    @Test
    fun `theme flows from dataSource`() =
        runTest {
            assertEquals("SYSTEM", repository.theme.first())
        }

    @Test
    fun `setTheme delegates to dataSource`() =
        runTest {
            repository.setTheme("OCEAN")
            coVerify { dataStore.setTheme("OCEAN") }
        }
}
