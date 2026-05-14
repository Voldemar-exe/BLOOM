package com.example.habit

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.picture.BloomIcons
import com.example.plant.PlantConfig
import kotlinx.coroutines.delay

@Composable
fun PlantCloseUp(
    modifier: Modifier = Modifier,
    seed: Long,
    variability: Float,
    plantConfig: PlantConfig,
    extraButton: @Composable (() -> Unit),
) {
    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(1f) }
    var animatedProgress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(isPlaying) {
        if (!isPlaying) return@LaunchedEffect

        val start = animatedProgress
        val duration = 5000L
        val startTime = System.currentTimeMillis()

        while (isPlaying && animatedProgress <= 1f) {
            val t = (System.currentTimeMillis() - startTime) / duration.toFloat()
            animatedProgress = (start + t).coerceIn(0f, 1f)
            delay(16)
        }
    }

    Row(
        modifier =
            Modifier
                .height(250.dp)
                .fillMaxWidth()
                .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .border(1.dp, Color.Gray),
        ) {
            PlantCanvas(
                progress =
                    if ((!isPlaying && animatedProgress != 0f) || isPlaying) {
                        animatedProgress
                    } else {
                        progress
                    },
                seed = seed,
                variability = variability,
                config = plantConfig,
            )
        }

        Column {
            FilledIconButton(onClick = { isPlaying = !isPlaying }) {
                Icon(
                    painter =
                        painterResource(
                            if (isPlaying) BloomIcons.Pause else BloomIcons.Play,
                        ),
                    contentDescription = null,
                )
            }
            FilledIconButton(
                onClick = {
                    isPlaying = false
                    animatedProgress = 0f
                },
            ) {
                Icon(
                    painter = painterResource(BloomIcons.Stop),
                    contentDescription = "stop",
                )
            }
            // TODO: Add extra functional
//            extraButton()
        }
    }
}
