package com.example.profile.embedded.achievements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.profile.model.Achievement

@Composable
fun AchievementScreen(onBack: () -> Unit) {
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
        )
    }) { padding ->
        Box(Modifier
            .fillMaxSize()
            .padding(padding)) {
            Text(
                "Achievements Content",
                Modifier.align(Alignment.Center),
            )
        }
    }
}

@Composable
fun AchievementScreen(achievements: List<Achievement>) {
    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
        ) {
            items(Achievement.entries) { achievement ->
                AchievementItem(
                    achievement = achievement,
                    isCompleted = achievement in achievements,
                )
            }
        }
    }
}

@Composable
fun AchievementItem(
    achievement: Achievement,
    isCompleted: Boolean,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(
                color =
                    if (isCompleted) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        Color.LightGray
                    },
                shape = ShapeDefaults.Small,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier.size(56.dp),
            painter = painterResource(achievement.image),
            contentScale = ContentScale.FillBounds,
            contentDescription = null,
        )
        Spacer(Modifier.width(10.dp))
        Column {
            Text(achievement.title)
            Text(achievement.description)
        }
    }
}

@Preview
@Composable
private fun PrevAc() {
    AchievementScreen(emptyList())
}
