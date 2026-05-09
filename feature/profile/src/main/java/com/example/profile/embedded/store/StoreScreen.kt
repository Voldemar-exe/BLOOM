package com.example.profile.embedded.store

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.picture.BloomBackgrounds
import com.example.designsystem.picture.BloomColors
import com.example.designsystem.picture.BloomIcons
import com.example.gamification.model.StoreItem
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StoreScreen(
    onBack: () -> Unit,
    viewModel: StoreViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    StoreScreen(
        state = state,
        onAction = viewModel::onAction,
        onBack = onBack,
    )
}

@Composable
fun StoreScreen(
    state: StoreState,
    onAction: (StoreAction) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Магазин") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            "Назад",
                        )
                    }
                },
                actions = {
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
                            text = state.currency.toString(),
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
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
                    .padding(padding)
                    .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            item {
                StoreSection(
                    title = "Цвет",
                    items = state.colors,
                    onPurchase = { onAction(StoreAction.PurchaseColor(it)) },
                ) { storeItem ->
                    Icon(
                        painter = painterResource(BloomIcons.Circle),
                        tint = BloomColors.resolve(storeItem.item.key),
                        contentDescription = null,
                    )
                }
            }

            item {
                StoreSection(
                    title = "Фон",
                    items = state.backgrounds,
                    onPurchase = { onAction(StoreAction.PurchaseBackground(it)) },
                ) { storeItem ->
                    Image(
                        modifier = Modifier.size(56.dp),
                        painter =
                            painterResource(
                                BloomBackgrounds.resolve(storeItem.item.key),
                            ),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                    )
                }
            }

            item {
                StoreSection(
                    title = "Растения",
                    items = state.plants,
                    onPurchase = { onAction(StoreAction.PurchasePlant(it)) },
                ) { storeItem ->
                    Text(text = storeItem.item.key)
                }
            }
        }
    }
}

@Composable
private fun StoreSection(
    title: String,
    items: List<StoreItem>,
    onPurchase: (String) -> Unit,
    content: @Composable (StoreItem) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.height(200.dp),
        ) {
            items(items) { storeItem ->
                ShopGridItem(
                    item = storeItem,
                    isPurchased = storeItem.isPurchased,
                    onClick = { onPurchase(storeItem.item.key) },
                    content = content,
                )
            }
        }
    }
}

@Composable
fun ShopGridItem(
    item: StoreItem,
    isPurchased: Boolean,
    onClick: () -> Unit,
    content: @Composable (StoreItem) -> Unit,
) {
    Surface(
        modifier = Modifier.size(56.dp, 100.dp),
        shape = ShapeDefaults.Medium,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        tonalElevation = 1.dp,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                content(item)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(
                            color =
                                if (isPurchased) {
                                    MaterialTheme.colorScheme.surfaceContainerHighest
                                } else {
                                    MaterialTheme.colorScheme.primary
                                },
                        ).clickable { onClick() },
            ) {
                if (isPurchased) {
                    Text(
                        text = "Куплено",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                } else {
                    Text(
                        text = item.price.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "C",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        }
    }
}
