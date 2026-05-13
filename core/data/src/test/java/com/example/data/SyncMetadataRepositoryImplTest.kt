package com.example.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import com.example.data.repository.SyncMetadataRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.coroutines.cancellation.CancellationException

@RunWith(RobolectricTestRunner::class)
class SyncMetadataRepositoryImplTest {
    private val mockDataStore = mockk<DataStore<Preferences>>(relaxed = true)

    private lateinit var repository: SyncMetadataRepositoryImpl

    private val syncKey = longPreferencesKey("last_sync_timestamp")

    @Before
    fun setUp() {
        repository = SyncMetadataRepositoryImpl(mockDataStore)
    }

    @Test
    fun `getLastSyncTimestamp returns default 0 on empty`() =
        runTest {
            val prefs = mockk<Preferences>(relaxed = true)

            every { prefs[syncKey] } returns null
            every { mockDataStore.data } returns flowOf(prefs)

            assertEquals(0L, repository.getLastSyncTimestamp())
        }

    @Test
    fun `getLastSyncTimestamp returns saved value`() =
        runTest {
            val prefs = mockk<Preferences>(relaxed = true)

            every { prefs[syncKey] } returns 12345L
            every { mockDataStore.data } returns flowOf(prefs)

            assertEquals(12345L, repository.getLastSyncTimestamp())
        }

    @Test(expected = CancellationException::class)
    fun `getLastSyncTimestamp handles CancellationException`() =
        runTest {
            every { mockDataStore.data } throws CancellationException("test")

            repository.getLastSyncTimestamp()
        }

    @Test
    fun `saveLastSyncTimestamp updates dataStore`() =
        runTest {
            coEvery {
                mockDataStore.updateData(any())
            } returns mockk(relaxed = true)

            repository.saveLastSyncTimestamp(999L)

            coVerify(exactly = 1) {
                mockDataStore.updateData(any())
            }
        }
}
