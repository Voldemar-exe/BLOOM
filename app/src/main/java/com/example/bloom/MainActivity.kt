package com.example.bloom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.bloom.ui.BloomApp
import com.example.bloom.ui.NotificationPermissionRequester
import com.example.designsystem.model.AppTheme
import com.example.designsystem.theme.BLOOMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // TODO: Inject from dataStore
            val appTheme = AppTheme.RED_VINE
            BLOOMTheme(appTheme = appTheme) {
                NotificationPermissionRequester()
                BloomApp()
            }
        }
    }
}
