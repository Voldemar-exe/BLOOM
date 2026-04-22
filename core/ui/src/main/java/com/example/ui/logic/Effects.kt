package com.example.ui.logic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun <T : Any> SharedFlow<T>.CollectOneShotEffect(onEffect: (T) -> Unit) {
    LaunchedEffect(this) {
        this@CollectOneShotEffect.collect { onEffect(it) }
    }
}
