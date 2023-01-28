package com.example.habit.presentation.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.habit.domain.model.HabitCategory
import com.example.habit.presentation.statistics.components.CategoryAmountChart
import com.example.habit.presentation.statistics.components.CompletionTimeChart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    state: StatisticsScreenState,
    onEvent: (StatisticsScreenEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        //modifier = Modifier.verticalScroll(rememberScrollState()),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Statistics",
                        textAlign = TextAlign.Center)
                },
                navigationIcon = {
                    /*IconButton(onClick = {  }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowLeft,
                            contentDescription = "Localized description"
                        )
                    }*/
                },
                actions = {
                    /*IconButton(onClick = {  }) {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = "Localized description"
                        )
                    }*/
                },
                scrollBehavior = scrollBehavior)
        },
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
        Column(modifier = Modifier.padding(paddingValues).verticalScroll(rememberScrollState())) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Column() {
                    FilterChip(
                        selected = state.isCategorySelected,
                        onClick = { onEvent(StatisticsScreenEvent.OpenCategoryDropdown) },
                        label = {Text(state.categoryName)},
                        trailingIcon = { Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "Dropdown")})
                    DropdownMenu(
                        expanded = state.isCategoryDropdownDisplayed,
                        onDismissRequest = { onEvent(StatisticsScreenEvent.DismissCategoryDropdown) }) {
                        DropdownMenuItem(
                            text = { Text("All") },
                            onClick = { onEvent(StatisticsScreenEvent.SetCategory(HabitCategory(0,"Category"))) })
                        for(category in state.categories){
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = { onEvent(StatisticsScreenEvent.SetCategory(category)) })
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(4.dp))
                Column() {
                    FilterChip(
                        selected = true,
                        onClick = { onEvent(StatisticsScreenEvent.OpenPeriodDropdown) },
                        label = {Text(state.datePeriodString)},
                        trailingIcon = { Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "Dropdown")})
                    DropdownMenu(
                        expanded = state.isDatePeriodDropdownDisplayed,
                        onDismissRequest = { onEvent(StatisticsScreenEvent.DismissPeriodDropdown) }) {
                        DropdownMenuItem(text = { Text("7 days") }, onClick = { onEvent(StatisticsScreenEvent.SetPeriodInDays(7)) })
                        DropdownMenuItem(text = { Text("14 days") }, onClick = { onEvent(StatisticsScreenEvent.SetPeriodInDays(14)) })
                        DropdownMenuItem(text = { Text("30 days") }, onClick = { onEvent(StatisticsScreenEvent.SetPeriodInDays(30)) })
                        DropdownMenuItem(text = { Text("90 days") }, onClick = { onEvent(StatisticsScreenEvent.SetPeriodInDays(90)) })
                    }
                }
            }
            CategoryAmountChart(
                dataSet = state.categoryAmountChartDataSet
            )
            for (data in state.amountListDataSet){
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = data.name)
                    Text(text = data.amount.toString())
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Column() {
                    FilterChip(
                        selected = state.isHabitSelected,
                        onClick = { onEvent(StatisticsScreenEvent.OpenHabitDropdown) },
                        label = { Text(state.selectedHabitName) },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = "Dropdown"
                            )
                        },
                        enabled = state.isHabitChipEnabled)
                    DropdownMenu(
                        expanded = state.isHabitDropdownDisplayed,
                        onDismissRequest = { onEvent(StatisticsScreenEvent.DismissHabitDropdown) }) {
                        for (habit in state.habitNames) {
                            DropdownMenuItem(
                                text = { Text(habit.name) },
                                onClick = { onEvent(StatisticsScreenEvent.SetHabit(habit)) })
                        }
                    }
                }
            }
            CompletionTimeChart(
                startDate = state.startDate,
                endDate = state.endDate,
                perfectCompletionTime = state.perfectCompletionTime,
                dataSet = state.completionTimeDataSet
            )
        }
    }
}