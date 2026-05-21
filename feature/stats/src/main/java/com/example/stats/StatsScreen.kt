package com.example.stats

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.model.AppTheme
import com.example.designsystem.theme.BLOOMTheme
import com.example.designsystem.util.ThemePreviewProvider
import com.example.model.WeeklyBySource
import com.example.stats.model.HabitsVsTasksRatio
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.min

@Composable
fun StatsScreen(viewModel: StatsViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        viewModel.loadStats()
    }

    StatsScreen(
        state = state,
    )
}

@Composable
fun StatsScreen(state: StatsState) {
    Scaffold { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                MetricCard(
                    title = "Текущая серия",
                    value = "${state.currentStreak}",
                    subtitle = "макс: ${state.longestStreak}",
                    modifier = Modifier.weight(1f),
                )
                CoinCard(
                    current = state.currentCoinsAmount,
                    max = state.maxCoinsAmount,
                    modifier = Modifier.weight(1f),
                )
            }

            HabitsTasksPieChartCard(
                ratio = state.habitsVsTasksRatio,
            )

            HabitsSummaryCard(
                created = state.totalHabitsCreated,
                completed = state.totalHabitsCompleted,
            )

            TasksSummaryCard(
                created = state.totalTasksCreated,
                completed = state.totalTasksCompleted,
            )

            DetailedLineChartCard(
                data = state.weeklyCompletions,
                label = "Всего завершений",
                dayLabels = state.weekDaysLabels,
            )

            StackedBarChartCard(
                weeklyBySource = state.weeklyBySource,
                dayLabels = state.weekDaysLabels,
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        modifier = modifier,
        colors =
            CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            )
        }
    }
}

@Composable
private fun CoinCard(
    current: Int,
    max: Int,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        modifier = modifier,
        colors =
            CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Монеты",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$current",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "макс: $max",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
            )
        }
    }
}

@Composable
private fun HabitsTasksPieChartCard(ratio: HabitsVsTasksRatio) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Привычки vs Задачи",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Завершено всего",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                contentAlignment = Alignment.Center,
            ) {
                CirclePieChart(
                    habitsCompleted = ratio.completedHabits,
                    tasksCompleted = ratio.completedTasks,
                    modifier = Modifier.size(160.dp),
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                LegendItem(
                    color = MaterialTheme.colorScheme.primary,
                    label = "Привычки: ${ratio.completedHabits}",
                )
                LegendItem(
                    color = MaterialTheme.colorScheme.tertiary,
                    label = "Задачи: ${ratio.completedTasks}",
                )
            }
        }
    }
}

@Composable
private fun CirclePieChart(
    habitsCompleted: Int,
    tasksCompleted: Int,
    modifier: Modifier = Modifier,
) {
    val total = habitsCompleted + tasksCompleted
    val habitsRatio = if (total > 0) habitsCompleted.toFloat() / total else 0.5f
    val tasksRatio = 1f - habitsRatio

    val primaryColor = MaterialTheme.colorScheme.primary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface

    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = min(size.width, size.height) / 2 * 0.9f

        if (habitsRatio > 0) {
            drawArc(
                color = primaryColor,
                startAngle = -90f,
                sweepAngle = 360f * habitsRatio,
                useCenter = true,
                topLeft = center - Offset(radius, radius),
                size = Size(radius * 2, radius * 2),
            )
        }
        if (tasksRatio > 0) {
            drawArc(
                color = tertiaryColor,
                startAngle = -90f + 360f * habitsRatio,
                sweepAngle = 360f * tasksRatio,
                useCenter = true,
                topLeft = center - Offset(radius, radius),
                size = Size(radius * 2, radius * 2),
            )
        }
        drawCircle(
            color = surfaceColor,
            radius = radius * 0.5f,
            center = center,
        )
        drawIntoCanvas { canvas ->
            canvas.nativeCanvas.drawText(
                "$total",
                center.x,
                center.y + 10.dp.toPx(),
                Paint().apply {
                    color = onSurfaceColor.toArgb()
                    textSize = 24.sp.toPx()
                    textAlign = Paint.Align.CENTER
                    isFakeBoldText = true
                },
            )
        }
    }
}

@Composable
private fun LegendItem(
    color: Color,
    label: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .size(12.dp)
                    .padding(2.dp),
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(color = color)
            }
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun HabitsSummaryCard(
    created: Int,
    completed: Int,
) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Привычки",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    Text(text = "Создано", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = "$created",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Завершено", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = "$completed",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

@Composable
private fun TasksSummaryCard(
    created: Int,
    completed: Int,
) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Задачи",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    Text(text = "Создано", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = "$created",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Завершено", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = "$completed",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailedLineChartCard(
    data: List<Int>,
    label: String,
    dayLabels: List<String>,
) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(180.dp),
            ) {
                val textMeasurer = rememberTextMeasurer()
                DetailedLineChart(
                    data = data,
                    dayLabels = dayLabels,
                    textMeasurer = textMeasurer,
                )
            }
        }
    }
}

@Composable
private fun DetailedLineChart(
    data: List<Int>,
    dayLabels: List<String>,
    textMeasurer: TextMeasurer,
    modifier: Modifier = Modifier,
) {
    if (data.isEmpty()) return

    val maxVal = (data.maxOrNull() ?: 0).coerceAtLeast(1)
    val stepX = 1f / (data.size - 1).coerceAtLeast(1)
    val primColor = MaterialTheme.colorScheme.primary
    val surColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val gridColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val paddingTop = 20.dp.toPx()
        val paddingBottom = 30.dp.toPx()
        val paddingLeft = 30.dp.toPx()
        val paddingRight = 10.dp.toPx()
        val chartWidth = width - paddingLeft - paddingRight
        val chartHeight = height - paddingTop - paddingBottom

        val gridLines = 4
        for (i in 0..gridLines) {
            val y = paddingTop + (chartHeight * i / gridLines)
            drawLine(
                color = gridColor,
                start = Offset(paddingLeft, y),
                end = Offset(width - paddingRight, y),
                strokeWidth = 1.dp.toPx(),
            )
            val value = maxVal - (maxVal * i / gridLines)
            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawText(
                    "$value",
                    paddingLeft - 8.dp.toPx(),
                    y + 4.dp.toPx(),
                    Paint().apply {
                        color = onSurfaceColor.copy(alpha = 0.6f).toArgb()
                        textSize = 10.sp.toPx()
                        textAlign = Paint.Align.RIGHT
                    },
                )
            }
        }

        drawLine(
            color = onSurfaceColor.copy(alpha = 0.3f),
            start = Offset(paddingLeft, height - paddingBottom),
            end = Offset(width - paddingRight, height - paddingBottom),
            strokeWidth = 1.dp.toPx(),
        )

        val points =
            data.mapIndexed { index, value ->
                val x = paddingLeft + index * stepX * chartWidth
                val y =
                    height - paddingBottom -
                        (value.toFloat() / maxVal) * chartHeight
                Offset(x, y)
            }

        if (points.size >= 2) {
            val areaPath =
                Path().apply {
                    moveTo(points.first().x, height - paddingBottom)
                    points.forEach { point ->
                        lineTo(point.x, point.y)
                    }
                    lineTo(points.last().x, height - paddingBottom)
                    close()
                }
            drawPath(
                path = areaPath,
                brush =
                    Brush.verticalGradient(
                        colors =
                            listOf(
                                primColor.copy(alpha = 0.2f),
                                primColor.copy(alpha = 0.05f),
                            ),
                        startY = points.minOf { it.y },
                        endY = height - paddingBottom,
                    ),
            )
        }

        val linePath =
            Path().apply {
                if (points.isNotEmpty()) {
                    moveTo(points.first().x, points.first().y)
                    for (i in 1 until points.size) {
                        lineTo(points[i].x, points[i].y)
                    }
                }
            }
        drawPath(
            path = linePath,
            color = primColor,
            style = Stroke(width = 3.dp.toPx()),
        )

        points.forEachIndexed { index, point ->
            drawCircle(
                color = surColor,
                radius = 6.dp.toPx(),
                center = point,
            )
            drawCircle(
                color = primColor,
                radius = 4.dp.toPx(),
                center = point,
            )
            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawText(
                    "${data[index]}",
                    point.x,
                    point.y - 12.dp.toPx(),
                    Paint().apply {
                        color = onSurfaceColor.toArgb()
                        textSize = 11.sp.toPx()
                        textAlign = Paint.Align.CENTER
                        isFakeBoldText = true
                    },
                )
            }
            if (index < dayLabels.size) {
                drawIntoCanvas { canvas ->
                    canvas.nativeCanvas.drawText(
                        dayLabels[index],
                        point.x,
                        height - paddingBottom + 16.dp.toPx(),
                        Paint().apply {
                            color = onSurfaceColor.copy(alpha = 0.7f).toArgb()
                            textSize = 10.sp.toPx()
                            textAlign = Paint.Align.CENTER
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun StackedBarChartCard(
    weeklyBySource: WeeklyBySource,
    dayLabels: List<String>,
) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Активность по типам",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                LegendItem(
                    color = MaterialTheme.colorScheme.primary,
                    label = "Привычки",
                )
                LegendItem(
                    color = MaterialTheme.colorScheme.tertiary,
                    label = "Задачи",
                )
                LegendItem(
                    color = MaterialTheme.colorScheme.secondary,
                    label = "Достижения",
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(180.dp),
            ) {
                StackedBarChart(
                    habits = weeklyBySource.habitsCompletions,
                    tasks = weeklyBySource.tasksCompletions,
                    achievements = weeklyBySource.achievementsUnlocks,
                    dayLabels = dayLabels,
                )
            }
        }
    }
}

@Composable
private fun StackedBarChart(
    habits: List<Int>,
    tasks: List<Int>,
    achievements: List<Int>,
    dayLabels: List<String>,
    modifier: Modifier = Modifier,
) {
    if (habits.isEmpty()) return

    val data = habits.zip(tasks).zip(achievements).map { (ht, a) -> ht.first + ht.second + a }
    val maxVal = (data.maxOrNull() ?: 0).coerceAtLeast(1)
    val primColor = MaterialTheme.colorScheme.primary
    val terColor = MaterialTheme.colorScheme.tertiary
    val secColor = MaterialTheme.colorScheme.secondary
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val gridColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val paddingTop = 10.dp.toPx()
        val paddingBottom = 30.dp.toPx()
        val paddingLeft = 20.dp.toPx()
        val paddingRight = 10.dp.toPx()
        val barSpacing = 8.dp.toPx()
        val chartWidth = width - paddingLeft - paddingRight
        val chartHeight = height - paddingTop - paddingBottom
        val barWidth = (chartWidth - (data.size + 1) * barSpacing) / data.size

        val gridLines = 3
        for (i in 0..gridLines) {
            val y = paddingTop + (chartHeight * i / gridLines)
            drawLine(
                color = gridColor,
                start = Offset(paddingLeft, y),
                end = Offset(width - paddingRight, y),
                strokeWidth = 1.dp.toPx(),
            )
            val value = maxVal - (maxVal * i / gridLines)
            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawText(
                    "$value",
                    paddingLeft - 8.dp.toPx(),
                    y + 4.dp.toPx(),
                    Paint().apply {
                        color = onSurfaceColor.copy(alpha = 0.6f).toArgb()
                        textSize = 10.sp.toPx()
                        textAlign = Paint.Align.RIGHT
                    },
                )
            }
        }

        drawLine(
            color = onSurfaceColor.copy(alpha = 0.3f),
            start = Offset(paddingLeft, height - paddingBottom),
            end = Offset(width - paddingRight, height - paddingBottom),
            strokeWidth = 1.dp.toPx(),
        )

        data.forEachIndexed { index, total ->
            if (total <= 0) return@forEachIndexed
            val x = paddingLeft + barSpacing + index * (barWidth + barSpacing)
            val barHeight = (total.toFloat() / maxVal) * chartHeight
            var currentY = height - paddingBottom

            if (habits[index] > 0) {
                val segHeight = (habits[index].toFloat() / maxVal) * chartHeight
                drawRoundRect(
                    color = primColor,
                    topLeft = Offset(x, currentY - segHeight),
                    size = Size(barWidth, segHeight),
                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx()),
                )
                currentY -= segHeight
            }
            if (tasks[index] > 0) {
                val segHeight = (tasks[index].toFloat() / maxVal) * chartHeight
                drawRoundRect(
                    color = terColor,
                    topLeft = Offset(x, currentY - segHeight),
                    size = Size(barWidth, segHeight),
                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx()),
                )
                currentY -= segHeight
            }
            if (achievements[index] > 0) {
                val segHeight = (achievements[index].toFloat() / maxVal) * chartHeight
                drawRoundRect(
                    color = secColor,
                    topLeft = Offset(x, currentY - segHeight),
                    size = Size(barWidth, segHeight),
                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx()),
                )
            }

            if (index < dayLabels.size) {
                drawIntoCanvas { canvas ->
                    canvas.nativeCanvas.drawText(
                        dayLabels[index],
                        x + barWidth / 2,
                        height - paddingBottom + 16.dp.toPx(),
                        Paint().apply {
                            color = onSurfaceColor.copy(alpha = 0.7f).toArgb()
                            textSize = 10.sp.toPx()
                            textAlign = Paint.Align.CENTER
                        },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StatsScreenPreview(
    @PreviewParameter(ThemePreviewProvider::class)
    appTheme: AppTheme,
) {
    BLOOMTheme(appTheme = appTheme) {
        StatsScreen(
            state =
                StatsState(
                    currentExperience = 450,
                    experienceToNextLevel = 500,
                    currentCoinsAmount = 1250,
                    maxCoinsAmount = 5000,
                    totalHabitsCreated = 12,
                    totalHabitsCompleted = 85,
                    totalTasksCreated = 34,
                    totalTasksCompleted = 120,
                    currentStreak = 7,
                    longestStreak = 14,
                    weeklyCompletions = listOf(1, 2, 1, 3, 2, 4, 3),
                    weeklyBySource =
                        WeeklyBySource(
                            habitsCompletions = listOf(1, 1, 0, 2, 1, 2, 1),
                            tasksCompletions = listOf(0, 1, 1, 1, 1, 2, 2),
                            achievementsUnlocks = listOf(0, 0, 0, 0, 0, 0, 0),
                        ),
                    habitsVsTasksRatio =
                        HabitsVsTasksRatio(
                            completedHabits = 85,
                            completedTasks = 120,
                        ),
                ),
        )
    }
}
