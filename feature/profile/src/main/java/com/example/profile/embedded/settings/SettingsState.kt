package com.example.profile.embedded.settings

import androidx.compose.runtime.Immutable
import com.example.model.AppSettings

@Immutable
data class SettingsState(val settings: AppSettings = AppSettings.default())
