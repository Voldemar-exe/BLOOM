package com.example.profile.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bloom.feature.profile.R
import com.example.designsystem.picture.BloomIcons
import com.example.ui.logic.CollectOneShotEffect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onNavigate: (ProfileEvent) -> Unit,
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.events.CollectOneShotEffect { event ->
        onNavigate(event)
    }

    if (state.user == null) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            EnterAccountPlaceholder(viewModel::onAction)
        }
    } else {
        ProfileScreen(
            state = state,
            onAction = viewModel::onAction,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProfileScreen(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.screen_title))
                },
                navigationIcon = {
                    IconButton(onClick = { /* Edit data */ }) {
                        Icon(Icons.Default.Edit, contentDescription = "notifications")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Notifications */ }) {
                        Icon(Icons.Default.Notifications, contentDescription = "notifications")
                    }
                    IconButton(onClick = { onAction(ProfileAction.OnSettingsClick) }) {
                        Icon(Icons.Default.Settings, contentDescription = "settings")
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            val user = state.user!!
            UserProfile(
                username = user.username,
                level = state.stats.level,
                coins = state.stats.currentCoinsAmount,
                background = user.background,
                avatar = user.avatar,
                color = Color(user.color),
            )
            LazyColumn(Modifier.fillMaxSize()) {
                item {
                    ProfileMenuSection(
                        icon = BloomIcons.Customization,
                        title = stringResource(R.string.customization_label),
                        items =
                            listOf(
                                MenuItem(
                                    BloomIcons.EditParameters,
                                    stringResource(R.string.edit_parameters_label),
                                ) {
                                    onAction(
                                        ProfileAction.OnParametersClick,
                                    )
                                },
                                MenuItem(
                                    BloomIcons.Image,
                                    stringResource(R.string.change_avatar_label),
                                ) { onAction(ProfileAction.OnAvatarClick) },
                                MenuItem(
                                    BloomIcons.ColorFill,
                                    stringResource(R.string.change_theme_label),
                                ) { onAction(ProfileAction.OnThemeClick) },
                            ),
                    )
                }

                item {
                    ProfileMenuSection(
                        icon = BloomIcons.Gamepad,
                        title = stringResource(R.string.gamification_label),
                        items =
                            listOf(
                                MenuItem(
                                    BloomIcons.Achievements,
                                    stringResource(R.string.achievements_label),
                                ) {
                                    onAction(
                                        ProfileAction.OnAchievementsClick,
                                    )
                                },
                                MenuItem(
                                    BloomIcons.Store,
                                    stringResource(R.string.store_label),
                                ) { onAction(ProfileAction.OnStoreClick) },
                                MenuItem(
                                    BloomIcons.RatingStar,
                                    stringResource(R.string.rating_label),
                                ) { onAction(ProfileAction.OnLeaderboardClick) },
                            ),
                    )
                }
            }
        }
    }
}

@Composable
fun UserProfile(
    username: String,
//    rankTitle: String,
//    experience: Long,
//    progress: Float,
    level: Int,
    coins: Int,
    background: Int,
    avatar: Int,
    color: Color,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // TODO: Maybe move in TopBar
        Row(
            modifier =
                Modifier
                    .background(color = Color.Red, shape = ShapeDefaults.Medium)
                    .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            // TODO: Add Rank Title ENUM based on level
            Text(modifier = Modifier.padding(16.dp), text = "Звание")
        }
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(216.dp),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(background),
                contentScale = ContentScale.Crop,
                contentDescription = "background",
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    modifier = Modifier.size(120.dp),
                    painter = painterResource(avatar),
                    contentDescription = "avatar",
                )
                Row(
                    modifier =
                        Modifier.background(color = color, shape = ShapeDefaults.ExtraLarge),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(modifier = Modifier.padding(16.dp), text = username)
                }
            }
        }
        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LinearProgressIndicator(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(horizontal = 32.dp),
                progress = { 0f }, // TODO: Add calculation based on level and experience
            )
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ) {
                Box(
                    modifier = Modifier.size(40.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = level.toString(), fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier =
                    Modifier
                        .background(Color(0xFFE8EAF6), RoundedCornerShape(16.dp))
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .wrapContentSize(),
            ) {
                Icon(
                    painter = painterResource(BloomIcons.PiggyBank),
                    contentDescription = "coins",
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = coins.toString(),
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
private fun ProfileMenuSection(
    icon: Int,
    title: String,
    items: List<MenuItem>,
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(painter = painterResource(icon), contentDescription = "icon")
            Spacer(Modifier.padding(horizontal = 4.dp))
            Text(
                text = title,
            )
        }
        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
        ) {
            items.forEach { item ->
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clickable { item.onClick() }
                            .padding(vertical = 16.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(item.icon),
                        contentDescription = item.title,
                        tint = Color(0xFF7E57C2),
                        modifier = Modifier.size(24.dp),
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = item.title,
                        modifier = Modifier.weight(1f),
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                        contentDescription = "Navigate",
                    )
                }
                if (item != items.last()) {
                    HorizontalDivider(Modifier, thickness = 1.dp)
                }
            }
        }
    }
}

private data class MenuItem(
    val icon: Int,
    val title: String,
    val onClick: () -> Unit,
)

@Composable
fun EnterAccountPlaceholder(onAction: (ProfileAction) -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // TODO: Navigate to :feature:auth
            Button(onClick = { onAction(ProfileAction.TestActionSetUser) }) {
                Text("Создать аккаунт")
            }
        }
    }
}
