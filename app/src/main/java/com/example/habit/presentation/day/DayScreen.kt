package com.example.habit.presentation.day

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.habit.R
import com.example.habit.presentation.day.components.HabitHistoryItemCard
import com.example.habit.presentation.day.components.HabitHistoryItemEditor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayScreen(
    state: DayScreenState,
    onEvent: (DayScreenEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var direction by remember { mutableStateOf(-1)}

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = state.dateStringResource?.let { stringResource(id = state.dateStringResource) }
                            ?: state.dateString
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onEvent(DayScreenEvent.MinusOneDay) },
                        enabled = state.isPriorDataAvailable
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowLeft,
                            contentDescription = ""
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {  }) {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = ""
                        )
                    }
                    IconButton(
                        onClick = { onEvent(DayScreenEvent.PlusOneDay) },
                        enabled = state.isFutureDataAvailable
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowRight,
                            contentDescription = ""
                        )
                    }
                },
                scrollBehavior = scrollBehavior)
        },
        bottomBar = {
            NavigationBar() {
                NavigationBarItem(
                    selected = false,
                    icon = { Icon(Icons.Filled.AutoGraph, contentDescription = "") },
                    label = { Text(stringResource(id = R.string.statistics)) },
                    onClick = { onEvent(DayScreenEvent.NavigateToStatistics) })
                NavigationBarItem(
                    selected = true,
                    icon = { Icon(Icons.Filled.CheckBox, contentDescription = "") },
                    label = { Text(stringResource(id = R.string.today)) },
                    onClick = { /*TODO*/ })
                NavigationBarItem(
                    selected = false,
                    icon = { Icon(Icons.Filled.AllInbox, contentDescription = "") },
                    label = { Text(stringResource(id = R.string.my_habits)) },
                    onClick = { onEvent(DayScreenEvent.NavigateToHabitsScreen) })
            }
        },

    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .pointerInput(state.isFutureDataAvailable, state.isPriorDataAvailable) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        when {
                            dragAmount.x > 0 -> {
                                //right
                                direction = 0
                            }
                            dragAmount.x < 0 -> {
                                // left
                                direction = 1
                            }
                        }
                    },
                    onDragEnd = {
                        when (direction) {
                            0 -> {
                                // right swipe code here
                                if(state.isPriorDataAvailable){
                                    onEvent(DayScreenEvent.MinusOneDay)
                                }
                            }
                            1 -> {
                                // left swipe code here
                                if(state.isFutureDataAvailable){
                                    onEvent(DayScreenEvent.PlusOneDay)
                                }
                            }
                        }
                    }
                )
            }
            .padding(paddingValues)
            .alpha(if (state.isItemEditorOpen) 0.7f else 1f))
        {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                contentPadding = PaddingValues(vertical = 4.dp, horizontal = 4.dp)
            ){
                items(state.habitHistoryItems){ item ->
                    HabitHistoryItemCard(habitHistoryItem = item, onEvent)
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Bottom
        ) {
            if (state.isItemEditorOpen) HabitHistoryItemEditor(
                itemId = state.selectedItemId,
                time = state.selectedItemTime,
                onEvent = onEvent
            )
        }
    }
    

}