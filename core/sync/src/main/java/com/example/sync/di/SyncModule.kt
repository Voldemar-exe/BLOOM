package com.example.sync.di

import com.example.sync.SyncWorker
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val syncModule =
    module {
        worker<SyncWorker> {
            SyncWorker(
                context = get(),
                workerParams = get(),
                syncRepository = get(),
                syncMetadataRepository = get(),
            )
        }
    }
