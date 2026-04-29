package com.example.bloom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.bloom.ui.BloomApp
import com.example.bloom.ui.NotificationPermissionRequester
import com.example.bloom.ui.theme.BLOOMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BLOOMTheme {
                NotificationPermissionRequester()
                BloomApp()
            }
        }
    }
}
