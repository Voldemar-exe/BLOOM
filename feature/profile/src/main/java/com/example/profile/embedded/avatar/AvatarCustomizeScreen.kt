package com.example.profile.embedded.avatar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.picture.BloomAvatars
import com.example.designsystem.picture.BloomBackgrounds
import com.example.designsystem.picture.BloomColors
import com.example.model.CustomizationItem
import com.example.model.CustomizationType
import com.example.profile.home.ProfileAvatarBox
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AvatarCustomizeScreen(
    onBack: () -> Unit,
    viewModel: AvatarCustomizeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AvatarCustomizeScreen(
        state = state,
        onAction = viewModel::onAction,
        onBack = onBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvatarCustomizeScreen(
    state: AvatarCustomizeState,
    onAction: (AvatarCustomizeAction) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настройка аватара") },
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
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                ProfileAvatarBox(
                    background = state.backgroundKey,
                    avatar = state.avatarKey,
                    color = state.colorKey,
                    username = state.username,
                    email = state.email,
                )
            }

            item {
                CustomizationSection(
                    title = "Аватар",
                    items =
                        BloomAvatars.entries.map {
                            CustomizationItem(
                                it.name,
                                CustomizationType.AVATAR,
                            )
                        },
                    selectedKey = state.avatarKey,
                    ownedItems = state.ownedItems,
                    onClick = { onAction(AvatarCustomizeAction.OnAvatarSelect(it)) },
                )
            }

            item {
                CustomizationSection(
                    title = "Задний фон",
                    items =
                        BloomBackgrounds.entries.map {
                            CustomizationItem(
                                it.name,
                                CustomizationType.BACKGROUND,
                            )
                        },
                    selectedKey = state.backgroundKey,
                    ownedItems = state.ownedItems,
                    onClick = { onAction(AvatarCustomizeAction.OnBackgroundSelect(it)) },
                )
            }

            item {
                CustomizationSection(
                    title = "Цвет",
                    items =
                        BloomColors.entries.map {
                            CustomizationItem(
                                it.name,
                                CustomizationType.COLOR,
                            )
                        },
                    selectedKey = state.colorKey,
                    ownedItems = state.ownedItems,
                    onClick = { onAction(AvatarCustomizeAction.OnColorSelect(it)) },
                )
            }
        }
    }
}

@Composable
private fun CustomizationSection(
    title: String,
    items: List<CustomizationItem>,
    ownedItems: List<CustomizationItem>,
    selectedKey: String,
    onClick: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            items(items) { item ->
                val owned = ownedItems.any { it.key == item.key }

                CustomizationCard(
                    item = item,
                    isSelected = item.key == selectedKey,
                    isOwned = owned,
                    onClick = {
                        if (owned) onClick(item.key)
                    },
                )
            }
        }
    }
}

@Composable
private fun CustomizationCard(
    item: CustomizationItem,
    isSelected: Boolean,
    isOwned: Boolean,
    onClick: () -> Unit,
) {
    Card(
        modifier =
            Modifier.size(88.dp),
        onClick = onClick,
        border =
            if (isSelected) {
                BorderStroke(
                    2.dp,
                    MaterialTheme.colorScheme.primary,
                )
            } else {
                null
            },
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            when (item.type) {
                CustomizationType.AVATAR -> {
                    Image(
                        painter = painterResource(BloomAvatars.resolve(item.key)),
                        contentDescription = "avatar",
                    )
                }

                CustomizationType.BACKGROUND -> {
                    Image(
                        painter = painterResource(BloomBackgrounds.resolve(item.key)),
                        contentDescription = "background",
                        contentScale = ContentScale.Crop,
                    )
                }

                CustomizationType.COLOR -> {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .background(BloomColors.resolve(item.key)),
                    )
                }

                else -> {}
            }

            if (!isOwned) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.6f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color.White,
                    )
                }
            }
        }
    }
}
