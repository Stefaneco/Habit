package com.example.habit.presentation.day

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.example.habit.presentation.day.components.HabitHistoryItemCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayScreen(
    state: DayScreenState,
    onEvent: (DayScreenEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = state.dateString)
                },
                navigationIcon = {
                    IconButton(onClick = { onEvent(DayScreenEvent.MinusOneDay) }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowLeft,
                            contentDescription = "Localized description"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {  }) {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = "Localized description"
                        )
                    }
                    IconButton(onClick = { onEvent(DayScreenEvent.PlusOneDay) }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowRight,
                            contentDescription = "Localized description"
                        )
                    }
                },
                scrollBehavior = scrollBehavior)
        },
        bottomBar = {
            NavigationBar() {
                NavigationBarItem(
                    selected = true,
                    icon = { Icon(Icons.Filled.CheckBox, contentDescription = "") },
                    label = { Text("Today") },
                    onClick = { /*TODO*/ })
                NavigationBarItem(
                    selected = false,
                    icon = { Icon(Icons.Filled.AllInbox, contentDescription = "") },
                    label = { Text("My Habits") },
                    onClick = { onEvent(DayScreenEvent.NavigateToHabitsScreen) })
            }
        },

    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                contentPadding = PaddingValues(vertical = 4.dp, horizontal = 4.dp)
            ){
                items(state.habitHistoryItems){ item ->
                    HabitHistoryItemCard(habitHistoryItem = item, onEvent)
                }
            }
        }
    }
    

}