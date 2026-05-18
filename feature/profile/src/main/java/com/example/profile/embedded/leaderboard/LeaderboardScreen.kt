package com.example.profile.embedded.leaderboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.model.AppTheme
import com.example.designsystem.picture.BloomAvatars
import com.example.designsystem.theme.BLOOMTheme
import com.example.designsystem.util.ThemePreviewProvider
import com.example.model.LeaderboardUser
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LeaderboardScreen(
    onBack: () -> Unit,
    viewModel: LeaderboardViewModel = koinViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    LeaderboardScreen(
        state = state,
        onAction = viewModel::onAction,
        onBack = onBack,
    )
}

@Composable
fun LeaderboardScreen(
    state: LeaderboardState,
    onAction: (LeaderboardAction) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Рейтинг") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                },
            )
        },
    ) { paddingValues ->

        if (state.isLoading) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                contentAlignment = Alignment.Center,
            ) {
                Text("Загрузка...")
            }
            return@Scaffold
        }

        if (state.users.isEmpty()) {
            EmptyLeaderboard(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                onRefresh = { onAction(LeaderboardAction.RefreshLeaderboard) },
            )
            return@Scaffold
        }

        val sortedUsers = state.users.sortedByDescending { it.score }

        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            itemsIndexed(sortedUsers) { index, user ->
                LeaderboardItem(
                    rank = index + 1,
                    user = user,
                )
            }
        }
    }
}

@Composable
fun EmptyLeaderboard(
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Рейтинг пока пуст",
            style = MaterialTheme.typography.titleMedium,
        )

        Spacer(modifier = Modifier.size(12.dp))

        Text(
            text = "Попробуйте обновить данные",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.size(16.dp))

        androidx.compose.material3.Button(
            onClick = onRefresh,
        ) {
            Text("Обновить")
        }
    }
}

@Composable
fun LeaderboardItem(
    rank: Int,
    user: LeaderboardUser,
    modifier: Modifier = Modifier,
) {
    val backgroundTint =
        when (rank) {
            1 -> Color(0xFFFFD700).copy(alpha = 0.2f)
            2 -> Color(0xFFC0C0C0).copy(alpha = 0.5f)
            3 -> Color(0xFFCD7F32).copy(alpha = 0.2f)
            else -> MaterialTheme.colorScheme.surfaceContainerLow
        }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .background(backgroundTint)
                    .padding(16.dp)
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier.size(52.dp),
                painter = painterResource(BloomAvatars.resolve(user.avatarKey)),
                contentDescription = "avatar",
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )

                Text(
                    text = "${user.score} очков",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Box(
                modifier =
                    Modifier
                        .size(38.dp)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            RoundedCornerShape(10.dp),
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = rank.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LeaderboardScreenPreview(
    @PreviewParameter(ThemePreviewProvider::class)
    appTheme: AppTheme,
) {
    val fakeUsers =
        listOf(
            LeaderboardUser(id = 0, "User", "JUST_GUY", 1500),
            LeaderboardUser(id = 1, "User2", "JUST_GUY", 1450),
            LeaderboardUser(id = 2, "User3", "JUST_GUY", 1300),
            LeaderboardUser(id = 3, "User4", "JUST_GUY", 1200),
            LeaderboardUser(id = 4, "User5", "JUST_GUY", 1100),
        )

    BLOOMTheme(appTheme = appTheme) {
        LeaderboardScreen(
                state = LeaderboardState(users = fakeUsers),
                onAction = {},
                onBack = {},
            )
    }
}
