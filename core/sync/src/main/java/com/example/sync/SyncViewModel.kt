package com.example.sync

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.SyncRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import timber.log.Timber

class SyncViewModel(private val repository: SyncRepository) : ViewModel() {
    init {
        viewModelScope.launch {
            repository
                .observePending()
                .distinctUntilChanged()
                .collectLatest { syncQueue ->
                    Timber.d("$syncQueue")
                }
        }
    }
}
