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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bloom.feature.profile.R
import com.example.designsystem.model.AppTheme
import com.example.designsystem.picture.BloomAvatars
import com.example.designsystem.picture.BloomBackgrounds
import com.example.designsystem.picture.BloomColors
import com.example.designsystem.picture.BloomIcons
import com.example.designsystem.theme.BLOOMTheme
import com.example.gamification.model.Rank
import com.example.model.util.XpRules
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
    val user = state.user!!
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.screen_title))
                },
                navigationIcon = {
                    var showDialog by remember { mutableStateOf(false) }
                    IconButton(onClick = { showDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "edit",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    if (showDialog) {
                        var username by rememberSaveable { mutableStateOf(user.username) }
                        var email by rememberSaveable { mutableStateOf(user.email) }
                        var password by rememberSaveable { mutableStateOf("") }

                        EditProfileDialog(
                            username = username,
                            email = email,
                            password = password,
                            onUsernameChange = { username = it },
                            onEmailChange = { email = it },
                            onPasswordChange = { password = it },
                            onDismiss = { showDialog = false },
                            onSaveClick = {
                                onAction(
                                    ProfileAction.OnUserUpdate(
                                        username,
                                        email,
                                        password,
                                    ),
                                )
                                showDialog = false
                            },
                        )
                    }
                },
                actions = {
                    /*IconButton(onClick = { *//* Notifications *//* }) {
                        Icon(Icons.Default.Notifications, contentDescription = "notifications")
                    }*/
                    IconButton(onClick = { onAction(ProfileAction.OnExitClick) }) {
                        Icon(Icons.AutoMirrored.Default.ExitToApp, contentDescription = "settings")
                    }
                    IconButton(onClick = { onAction(ProfileAction.OnSettingsClick) }) {
                        Icon(Icons.Default.Settings, contentDescription = "settings")
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            UserProfile(
                username = user.username,
                email = user.email,
                level = state.stats.level,
                experience = state.stats.currentExperience,
                coins = state.stats.currentCoinsAmount,
                background = user.backgroundKey,
                avatar = user.avatarKey,
                color = user.colorKey,
            )
            LazyColumn(Modifier.fillMaxSize()) {
                item {
                    ProfileMenuSection(
                        icon = BloomIcons.Customization,
                        title = stringResource(R.string.customization_label),
                        items =
                            listOf(
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
    email: String,
    level: Int,
    experience: Int,
    coins: Int,
    background: String,
    avatar: String,
    color: String,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val rank = Rank.fromLevel(level)
        val rankTheme = rank.getTheme(MaterialTheme.colorScheme)
        // TODO: Maybe move in TopBar
        Row(
            modifier =
                Modifier
                    .background(
                        brush = Brush.horizontalGradient(rankTheme.gradient),
                        shape = MaterialTheme.shapes.medium,
                    ).fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = rank.ru,
                style = MaterialTheme.typography.titleMedium,
                color = rankTheme.contentColor,
                fontWeight = FontWeight.SemiBold,
            )
        }

        ProfileAvatarBox(
            background = background,
            avatar = avatar,
            color = color,
            username = username,
            email = email,
        )

        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                Modifier
                    .weight(1f)
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                val totalXpForLevel = XpRules.xpToNextLevel(level)
                LinearProgressIndicator(
                    progress = { (experience.toFloat() / totalXpForLevel).coerceIn(0f, 1f) },
                )
                Text(
                    text = "$experience/$totalXpForLevel",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
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
                        .background(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = ShapeDefaults.Medium,
                        ).padding(horizontal = 8.dp, vertical = 6.dp)
                        .wrapContentSize(),
            ) {
                Icon(
                    painter = painterResource(BloomIcons.PiggyBank),
                    contentDescription = "coins",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = coins.toString(),
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
        }
    }
}

@Composable
fun ProfileAvatarBox(
    background: String,
    avatar: String,
    color: String,
    username: String,
    email: String,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(216.dp),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(BloomBackgrounds.resolve(background)),
            contentScale = ContentScale.Crop,
            contentDescription = "background",
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier = Modifier.size(120.dp),
                painter = painterResource(BloomAvatars.resolve(avatar)),
                contentDescription = "avatar",
            )
            Box(
                modifier =
                    Modifier.background(
                        color = BloomColors.resolve(color),
                        shape = ShapeDefaults.ExtraLarge,
                    ),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(text = username)
                    Text(
                        text = email,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
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
            Icon(
                painter = painterResource(icon),
                contentDescription = "icon",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.padding(horizontal = 4.dp))
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
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
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(24.dp),
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = item.title,
                        modifier = Modifier.weight(1f),
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        contentDescription = "Navigate",
                    )
                }
                if (item != items.last()) {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileDialog(
    username: String,
    email: String,
    password: String,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSaveClick: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Изменить данные")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedTextField(
                    value = username,
                    onValueChange = onUsernameChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("Имя пользователя") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                        )
                    },
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("Почта") },
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                        ),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = null,
                        )
                    },
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("Если нужна замена") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                        ),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                        )
                    },
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onSaveClick) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun RankPreview(
    @PreviewParameter(RankThemePreviewProvider::class)
    data: Pair<Rank, AppTheme>,
) {
    val (rank, appTheme) = data
    BLOOMTheme(appTheme = appTheme) {
        val rankTheme = rank.getTheme(MaterialTheme.colorScheme)

        Row(
            modifier =
                Modifier
                    .background(
                        brush = Brush.horizontalGradient(rankTheme.gradient),
                        shape = MaterialTheme.shapes.medium,
                    ).fillMaxWidth()
                    .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = rank.ru,
                style = MaterialTheme.typography.titleMedium,
                color = rankTheme.contentColor,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

class RankThemePreviewProvider : PreviewParameterProvider<Pair<Rank, AppTheme>> {
    override val values: Sequence<Pair<Rank, AppTheme>> =
        sequence {
            val themes = AppTheme.entries.asSequence()
            for (theme in themes) {
                for (rank in Rank.entries) {
                    yield(rank to theme)
                }
            }
        }
}
