package com.example.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.data.repository.SyncMetadataRepository
import com.example.data.repository.SyncRepository
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import kotlin.coroutines.cancellation.CancellationException

class SyncWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val syncRepository: SyncRepository,
    private val syncMetadataRepository: SyncMetadataRepository,
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result =
        withContext(Dispatchers.IO) {
            try {
                Timber.d("SyncWorker: starting")
                ensureActive()

                Timber.d("SyncWorker: pushChanges")
                syncRepository.pushChanges().getOrThrow()
                ensureActive()

                Timber.d("SyncWorker: pullChanges")
                val lastSync = syncMetadataRepository.getLastSyncTimestamp()
                val response =
                    withTimeoutOrNull(30_000) {
                        syncRepository.pullChanges(lastSync)
                    }?.getOrThrow() ?: throw TimeoutException("Pull timed out")

                syncMetadataRepository.saveLastSyncTimestamp(response.serverTimestamp)
                Timber.d("SyncWorker: completed")
                Result.success()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Timber.e(e, "SyncWorker: failed")
                return@withContext when (e) {
                    is UnknownHostException, is SocketTimeoutException, is HttpRequestTimeoutException ->
                        Result
                            .retry()

                    else -> Result.failure()
                }
            }
        }
}
