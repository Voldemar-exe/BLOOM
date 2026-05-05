package com.example.profile.embedded.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.model.AppTheme
import com.example.designsystem.model.previewColors
import com.example.designsystem.util.ThemeProvider
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ThemeChoiceScreen(
    onBack: () -> Unit = {},
    viewModel: ThemeChoiceViewModel = koinViewModel(),
) {
    val theme by viewModel.theme.collectAsStateWithLifecycle()

    ThemeChoiceScreen(
        currentTheme = AppTheme.valueOf(theme),
        onAction = viewModel::onAction,
        onBack = onBack,
    )
}

@Composable
fun ThemeChoiceScreen(
    currentTheme: AppTheme,
    onAction: (ThemeChoiceAction) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Выбор темы") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
                        )
                    }
                },
            )
        },
    ) { padding ->

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                ThemePreviewCard()
            }

            items(AppTheme.entries) { theme ->
                ThemeItem(
                    theme = theme,
                    selected = currentTheme == theme,
                    onClick = { onAction(ThemeChoiceAction.OnThemeItemClick(theme)) },
                )
            }
        }
    }
}

@Composable
private fun ThemePreviewCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Предпросмотр темы",
                style = MaterialTheme.typography.titleMedium,
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {}) {
                    Text("Button")
                }

                FilledIconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilledTonalButton(onClick = {}) {
                    Text("Tonal")
                }

                AssistChip(
                    onClick = {},
                    label = { Text("Chip") },
                )
            }

            LinearProgressIndicator(
                progress = { 0.7f },
                modifier = Modifier.fillMaxWidth(),
            )

            Checkbox(
                checked = true,
                onCheckedChange = {},
            )
        }
    }
}

@Composable
private fun ThemeItem(
    theme: AppTheme,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val borderColor =
        if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.outlineVariant
        }

    ElevatedCard(
        onClick = onClick,
        modifier =
            Modifier
                .fillMaxWidth()
                .aspectRatio(0.9f),
        colors =
            CardDefaults.elevatedCardColors(
                containerColor =
                    if (selected) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.surface
                    },
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ThemePalettePreview(theme)

            Text(
                text = theme.name,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                maxLines = 2,
            )

            RadioButton(
                selected = selected,
                onClick = null,
            )
        }
    }
}

@Composable
private fun ThemePalettePreview(
    theme: AppTheme,
    darkTheme: Boolean = isSystemInDarkTheme(),
) {
    val colors =
        when (theme) {
            AppTheme.SYSTEM -> {
                listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.secondary,
                    MaterialTheme.colorScheme.tertiary,
                )
            }

            else -> {
                ThemeProvider
                    .get(theme)
                    ?.previewColors(darkTheme)
                    ?: emptyList()
            }
        }

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        colors.forEach { color ->
            Box(
                modifier =
                    Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(color),
            )
        }
    }
}
