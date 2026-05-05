package com.example.bloom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bloom.ui.BloomApp
import com.example.bloom.ui.NotificationPermissionRequester
import com.example.data.repository.ThemeRepository
import com.example.designsystem.model.AppTheme
import com.example.designsystem.theme.BLOOMTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val themeRepository: ThemeRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appTheme by themeRepository.theme.collectAsStateWithLifecycle(
                initialValue = "SYSTEM",
                lifecycle = lifecycle,
            )
            BLOOMTheme(appTheme = AppTheme.valueOf(appTheme)) {
                NotificationPermissionRequester()
                BloomApp()
            }
        }
    }
}
