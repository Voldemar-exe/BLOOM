package com.example.profile.embedded.achievements

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.model.AppTheme
import com.example.designsystem.theme.BLOOMTheme
import com.example.designsystem.util.ThemePreviewProvider
import com.example.gamification.model.Achievement
import com.example.gamification.model.AchievementRegistry
import com.example.model.SortType
import com.example.model.UserStats
import com.example.ui.components.FilteredDropdownMenu
import org.koin.compose.viewmodel.koinViewModel
import timber.log.Timber
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

@Composable
fun AchievementScreen(
    onBack: () -> Unit,
    viewModel: AchievementViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AchievementScreen(
        state = state,
        onAction = viewModel::onAction,
        onBack = onBack,
    )
}

@Composable
fun AchievementScreen(
    state: AchievementState,
    onAction: (AchievementAction) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Достижения") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        "Назад",
                    )
                }
            },
            actions = {
                FilteredDropdownMenu(
                    selectedSortType = state.sortType,
                    onSortChange = { onAction(AchievementAction.OnSortChange(it)) },
                )
            },
        )
    }) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp),
        ) {
            items(state.sortedAchievementsWithProgress) { achievementWithProgress ->
                Timber.i("$achievementWithProgress")
                AchievementItem(
                    achievement = achievementWithProgress.first,
                    progress = achievementWithProgress.second,
                )
            }
        }
    }
}

@Composable
fun AchievementItem(
    achievement: Achievement,
    progress: Float,
    modifier: Modifier = Modifier,
) {
    val isCompleted = progress >= 1.0f

    val containerColor =
        if (isCompleted) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }

    val contentColor =
        if (isCompleted) {
            MaterialTheme.colorScheme.onPrimaryContainer
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f, fill = false),
            ) {
                if (isCompleted) {
                    val baseColor =
                        getColorFromId(
                            achievement.id,
                            MaterialTheme.colorScheme.primaryContainer,
                        )
                    AchievementBadge(baseColor = baseColor)
                } else {
                    Box(
                        modifier =
                            Modifier
                                .background(
                                    color = Color.Gray,
                                ).size(56.dp),
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = achievement.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = contentColor,
                        maxLines = 1,
                    )
                    Text(
                        text = achievement.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = contentColor.copy(alpha = 0.8f),
                        maxLines = 2,
                    )
                }
            }

            if (isCompleted) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Completed",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(24.dp),
                )
            } else {
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.labelLarge,
                    color = contentColor,
                    textAlign = TextAlign.End,
                )
            }
        }
    }
}

private fun getColorFromId(
    id: Int,
    surfaceColor: Color,
): Color {
    val hue = (id * 137.508f) % 360f
    var value = 0.9f
    val saturation = 0.7f

    val surfaceLum = getRelativeLuminance(surfaceColor)
    val step = if (surfaceLum > 0.5f) -0.05f else 0.05f

    repeat(30) {
        val candidate = Color.hsv(hue, saturation, value)
        if (getContrastRatio(candidate, surfaceColor) >= 4.5f) return candidate
        value = (value + step).coerceIn(0.05f, 0.95f)
    }

    return Color.hsv(hue, saturation, value)
}

private fun getRelativeLuminance(color: Color): Float {
    val r = color.red
    val g = color.green
    val b = color.blue
    val rLin = if (r <= 0.03928f) r / 12.92f else ((r + 0.055f) / 1.055f).pow(2.4f)
    val gLin = if (g <= 0.03928f) g / 12.92f else ((g + 0.055f) / 1.055f).pow(2.4f)
    val bLin = if (b <= 0.03928f) b / 12.92f else ((b + 0.055f) / 1.055f).pow(2.4f)
    return 0.2126f * rLin + 0.7152f * gLin + 0.0722f * bLin
}

private fun getContrastRatio(
    color1: Color,
    color2: Color,
): Float {
    val l1 = getRelativeLuminance(color1)
    val l2 = getRelativeLuminance(color2)
    val lighter = max(l1, l2)
    val darker = min(l1, l2)
    return (lighter + 0.05f) / (darker + 0.05f)
}

@Composable
fun AchievementBadge(
    baseColor: Color,
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition()

    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(10000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
            ),
        label = "colorPhase",
    )

    val colors =
        remember(baseColor, phase) {
            generateBadgeColors(baseColor, phase)
        }

    Box(
        modifier =
            modifier
                .size(64.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(
                    Brush.sweepGradient(
                        colors = listOf(colors.dark, colors.base, colors.light, colors.highlight),
                        center = Offset(0.5f, 0.5f),
                    ),
                ),
    ) {
        Box(
            Modifier
                .matchParentSize()
                .background(
                    Brush.radialGradient(
                        colors =
                            listOf(
                                Color.White.copy(alpha = 0.25f),
                                Color.Transparent,
                            ),
                        center = Offset(0.5f, 0.5f),
                    ),
                ),
        )
    }
}

private fun generateBadgeColors(
    base: Color,
    phase: Float,
): BadgeColors {
    val (h, s, v) = base.toHsv()
    val shiftedH = (h + phase) % 360f
    return BadgeColors(
        base = Color.hsv(shiftedH, s, v),
        light =
            Color.hsv(
                (shiftedH + 20f) % 360f,
                (s * 0.9f).coerceAtMost(1f),
                (v * 1.15f).coerceAtMost(1f),
            ),
        dark =
            Color.hsv(
                (shiftedH - 20f + 360f) % 360f,
                (s * 1.1f).coerceAtMost(1f),
                (v * 0.6f).coerceAtLeast(0f),
            ),
        highlight = Color.hsv((shiftedH + 40f) % 360f, 0.85f, 1f),
    )
}

data class BadgeColors(
    val base: Color,
    val light: Color,
    val dark: Color,
    val highlight: Color,
)

private fun Color.toHsv(): FloatArray {
    val r = red
    val g = green
    val b = blue
    val maxVal = max(r, max(g, b))
    val minVal = min(r, min(g, b))
    val delta = maxVal - minVal
    val h =
        when {
            delta == 0f -> 0f
            maxVal == r -> 60f * (((g - b) / delta) % 6f)
            maxVal == g -> 60f * (((b - r) / delta) + 2f)
            maxVal == b -> 60f * (((r - g) / delta) + 4f)
            else -> 0f
        }
    val s = if (maxVal == 0f) 0f else delta / maxVal
    val v = maxVal
    return floatArrayOf((h + 360f) % 360f, s, v)
}

@Preview(showBackground = true)
@Composable
private fun AchievementItemInProgressPreview(
    @PreviewParameter(ThemePreviewProvider::class)
    appTheme: AppTheme,
) {
    BLOOMTheme(appTheme = appTheme) {
        val stats =
            UserStats(
                level = 5,
                currentExperience = 450,
                totalHabitsCreated = 12,
                totalHabitsCompleted = 85,
                currentStreak = 7,
                longestStreak = 14,
                currentCoinsAmount = 1250,
                maxCoinsAmount = 1350,
                totalTasksCreated = 25,
                totalTasksCompleted = 58,
            )
        val withProgress =
            AchievementRegistry.allAchievements.map { achievement ->
                achievement to
                    getProgress(
                        achievement.condition,
                        stats,
                    )
            }

        val sortType = SortType.ID_ASC

        val sorted =
            when (sortType) {
                SortType.ID_ASC ->
                    withProgress.sortedBy { it.first.id }

                SortType.DESC ->
                    withProgress.sortedByDescending { it.second }

                SortType.ASC ->
                    withProgress.sortedBy { it.second }
            }

        AchievementScreen(
            state =
                AchievementState(
                    userStats = stats,
                    sortType = sortType,
                    sortedAchievementsWithProgress = sorted,
                ),
            onAction = {},
            onBack = {},
        )
    }
}
