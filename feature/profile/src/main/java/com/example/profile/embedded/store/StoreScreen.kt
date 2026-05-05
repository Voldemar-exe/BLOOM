package com.example.profile.embedded.store

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
                    AssistChip(
                        onClick = {},
                        label = { Text(state.currency.toString()) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(BloomIcons.PiggyBank),
                                contentDescription = null,
                            )
                        },
                    )
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
                        modifier = Modifier.padding(vertical = 4.dp),
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
    Column(
        modifier =
            Modifier
                .size(56.dp, 92.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = ShapeDefaults.Medium,
                ).padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Box(
            modifier = Modifier.size(56.dp),
            contentAlignment = Alignment.Center,
        ) {
            content(item)
        }
        AssistChip(
            onClick = onClick,
            label = {
                Text(
                    text = if (isPurchased) "Есть" else item.price.toString(),
                    style = MaterialTheme.typography.bodySmall,
                )
            },
        )
    }
}
