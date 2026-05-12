package com.example.bloom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.auth.AuthScreen
import com.example.bloom.ui.BloomApp
import com.example.bloom.ui.NotificationPermissionRequester
import com.example.data.repository.ThemeRepository
import com.example.designsystem.model.AppTheme
import com.example.designsystem.theme.BLOOMTheme
import com.example.sync.SyncViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val themeRepository: ThemeRepository by inject()
    private val mainViewModel: MainViewModel by viewModel()

    private val syncViewModel: SyncViewModel by viewModel()

    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val authState by mainViewModel.authState.collectAsStateWithLifecycle(
                initialValue = AuthState.Loading,
                lifecycle = lifecycle,
            )
            val appTheme by themeRepository.theme.collectAsStateWithLifecycle(
                initialValue = "SYSTEM",
                lifecycle = lifecycle,
            )

            syncViewModel

            BLOOMTheme(appTheme = AppTheme.valueOf(appTheme)) {
                when (authState) {
                    is AuthState.Loading ->
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularWavyProgressIndicator()
                        }

                    is AuthState.Authorized -> {
                        NotificationPermissionRequester()
                        BloomApp()
                    }

                    is AuthState.Unauthorized -> {
                        if ((authState as AuthState.Unauthorized).isSkipped) {
                            NotificationPermissionRequester()
                            BloomApp()
                        } else {
                            AuthScreen()
                        }
                    }
                }
            }
        }
    }
}
