package com.example.habit.presentation.statistics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    state: StatisticsScreenState,
    onEvent: (StatisticsScreenEvent) -> Unit
) {
    Scaffold(
        bottomBar = {
            NavigationBar() {
                NavigationBarItem(
                    selected = false,
                    icon = { Icon(Icons.Filled.CheckBox, contentDescription = "") },
                    label = { Text("Today") },
                    onClick = { onEvent(StatisticsScreenEvent.NavigateToDayScreen) })
                NavigationBarItem(
                    selected = false,
                    icon = { Icon(Icons.Filled.AllInbox, contentDescription = "") },
                    label = { Text("My Habits") },
                    onClick = { onEvent(StatisticsScreenEvent.NavigateToHabitsScreen) })
                NavigationBarItem(
                    selected = true,
                    icon = { Icon(Icons.Filled.AutoGraph, contentDescription = "") },
                    label = { Text("Statistics") },
                    onClick = {  })
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Text("Stats")
        }
    }
}