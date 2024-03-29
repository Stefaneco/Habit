package com.example.habit.presentation.habits

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.habit.R
import com.example.habit.presentation.habits.components.HabitCard
import com.example.habit.presentation.habits.components.HabitCreator
import com.example.habit.presentation.habits.components.HabitEditor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsScreen(
    state: HabitsScreenState,
    onEvent: (HabitsScreenEvent) -> Unit
) {
    Scaffold(
        bottomBar = {
            NavigationBar() {
                NavigationBarItem(
                    selected = false,
                    icon = { Icon(Icons.Filled.AutoGraph, contentDescription = "") },
                    label = { Text(stringResource(id = R.string.statistics)) },
                    onClick = { onEvent(HabitsScreenEvent.NavigateToStatistics) })
                NavigationBarItem(
                    selected = false,
                    icon = { Icon(Icons.Filled.CheckBox, contentDescription = "") },
                    label = { Text(stringResource(id = R.string.today)) },
                    onClick = { onEvent(HabitsScreenEvent.NavigateToDayScreen) })
                NavigationBarItem(
                    selected = true,
                    icon = { Icon(Icons.Filled.AllInbox, contentDescription = "") },
                    label = { Text(stringResource(id = R.string.my_habits)) },
                    onClick = { /*TODO*/ })

            }
        }
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .alpha(if (state.isNewHabitCreatorOpen || state.isHabitEditorOpen) 0.7f else 1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ){
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                contentPadding = PaddingValues(vertical = 4.dp, horizontal = 4.dp)
            ){
                items(state.habits) { habit ->
                    HabitCard(habit = habit, onEvent)
                }
            }
        }
        if(state.isNewHabitCreatorOpen) HabitCreator(
            name = state.newHabitName,
            time = state.newHabitTime,
            date = state.newHabitDate,
            repetition = state.newHabitRepetition,
            category = state.newHabitCategory,
            isValidHabit = state.isValidNewHabit,
            onEvent = onEvent,
            modifier = Modifier.padding(paddingValues)
        )
        else if (state.isHabitEditorOpen && state.editedHabit != null) {
            with(state.editedHabit){
                HabitEditor(
                    name = name,
                    time = start.time,
                    category = category.name,
                    isValidHabit = state.isValidEditedHabit,
                    onEvent = onEvent,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
        else Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            Button(onClick = { onEvent(HabitsScreenEvent.OpenNewHabitCreator) }) {
                Text(text = stringResource(id = R.string.new_habit))
            }
        }
    }
}
