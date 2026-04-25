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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.picture.BloomIcons
import com.example.plant.PlantConfig
import com.example.plant.utils.Randomizer

@Composable
fun PlantItem() {
}

@Composable
fun PlantCloseUp(
    modifier: Modifier = Modifier,
    seed: Long,
    variability: Float,
    plantConfig: PlantConfig,
) {
    Row(
        modifier =
            Modifier
                .height(250.dp)
                .fillMaxWidth()
                .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(modifier = Modifier.weight(1f).border(1.dp, Color.Gray)) {
            PlantCanvas(
                randomizer = Randomizer(seed),
                variability = variability,
                config = plantConfig,
                onAnimate = {},
                onStopAnimate = {},
                onDownload = {},
            )
        }

        Column {
            FilledIconButton(onClick = {}) {
                Icon(
                    painter = painterResource(BloomIcons.Play),
                    contentDescription = "play",
                )
            }
            FilledIconButton(onClick = {}) {
                Icon(
                    painter = painterResource(BloomIcons.Stop),
                    contentDescription = "stop",
                )
            }
            FilledIconButton(onClick = {}) {
                Icon(
                    painter = painterResource(BloomIcons.Download),
                    contentDescription = "download",
                )
            }
        }
    }
}
