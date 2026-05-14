package com.example.designsystem.util

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.designsystem.model.AppTheme

class ThemePreviewProvider : PreviewParameterProvider<AppTheme> {
    override val values: Sequence<AppTheme>
        get() = AppTheme.entries.asSequence()
}