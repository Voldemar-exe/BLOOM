package com.example.ui.components

import androidx.compose.animation.animateBounds
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.model.DayTimeInterval

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DayTimeTabs(
    selectedTab: DayTimeInterval,
    onTabClick: (DayTimeInterval) -> Unit,
    modifier: Modifier = Modifier,
) {
    LookaheadScope {
        Row(
            modifier = modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DayTimeInterval.entries.forEach { timeInterval ->
                ToggleButton(
                    checked = selectedTab == timeInterval,
                    onCheckedChange = { onTabClick(timeInterval) },
                    shapes =
                        when (timeInterval) {
                            DayTimeInterval.TODAY ->
                                ButtonGroupDefaults
                                    .connectedLeadingButtonShapes()

                            DayTimeInterval.TOMORROW ->
                                ButtonGroupDefaults
                                    .connectedMiddleButtonShapes()

                            DayTimeInterval.ALL ->
                                ButtonGroupDefaults
                                    .connectedTrailingButtonShapes()
                        },
                    modifier =
                        Modifier
                            .weight(
                                if (selectedTab == timeInterval) {
                                    1.5f
                                } else {
                                    1f
                                },
                            )
                            .semantics { role = Role.RadioButton }
                            .animateBounds(this@LookaheadScope),
                ) {
                    Text(
                        when (timeInterval) {
                            DayTimeInterval.TODAY -> "Сегодня"
                            DayTimeInterval.TOMORROW -> "Завтра"
                            DayTimeInterval.ALL -> "Все"
                        },
                    )
                }
            }
        }
    }
}
