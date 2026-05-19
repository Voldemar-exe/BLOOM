package com.example.habit

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationEndReason
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.picture.BloomIcons
import com.example.plant.PlantConfig
import kotlin.math.abs

@Composable
fun PlantCloseUp(
    modifier: Modifier = Modifier,
    seed: Long,
    realProgress: Float,
    variability: Float,
    plantConfig: PlantConfig,
    extraButton: @Composable (() -> Unit) = {},
) {
    var isPlaying by remember { mutableStateOf(false) }
    var isStopped by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(realProgress) }
    val animatable = remember { Animatable(0f) }

    LaunchedEffect(isPlaying, isStopped, progress) {
        if (isStopped) {
            isPlaying = false
            animatable.snapTo(0f)
            isStopped = false
        }
        if (!isPlaying) {
            animatable.stop()
            return@LaunchedEffect
        }

        val distance = abs(progress - animatable.value)
        val duration = (distance * 5000L).toLong().coerceIn(300L, 5000L)

        val result =
            animatable.animateTo(
                targetValue = progress,
                animationSpec =
                    tween(
                        durationMillis = duration.toInt(),
                        easing = FastOutSlowInEasing,
                    ),
            )

        if (result.endReason == AnimationEndReason.Finished) {
            isPlaying = false
            animatable.snapTo(0f)
        }
    }

    Row(
        modifier =
            modifier
                .height(250.dp)
                .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .border(1.dp, MaterialTheme.colorScheme.surfaceContainerHigh),
        ) {
            PlantCanvas(
                progress =
                    if ((!isPlaying && animatable.value != 0f) || isPlaying) {
                        animatable.value
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
            FilledIconButton(onClick = { isStopped = true },) {
                Icon(
                    painter = painterResource(BloomIcons.Stop),
                    contentDescription = "stop",
                )
            }
            extraButton()
        }
    }
}
