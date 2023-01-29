package com.example.habit.presentation.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.habit.R
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
                    selected = true,
                    icon = { Icon(Icons.Filled.AutoGraph, contentDescription = "") },
                    label = { Text(stringResource(id = R.string.statistics)) },
                    onClick = {  })
                NavigationBarItem(
                    selected = false,
                    icon = { Icon(Icons.Filled.CheckBox, contentDescription = "") },
                    label = { Text(stringResource(id = R.string.today)) },
                    onClick = { onEvent(StatisticsScreenEvent.NavigateToDayScreen) })
                NavigationBarItem(
                    selected = false,
                    icon = { Icon(Icons.Filled.AllInbox, contentDescription = "") },
                    label = { Text(stringResource(id = R.string.my_habits)) },
                    onClick = { onEvent(StatisticsScreenEvent.NavigateToHabitsScreen) })
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Column() {
                    val allCategoriesString = stringResource(id = R.string.all_categories)
                    FilterChip(
                        selected = state.isCategorySelected,
                        onClick = { onEvent(StatisticsScreenEvent.OpenCategoryDropdown) },
                        label = {Text(
                            state.categoryName ?: stringResource(id = R.string.all_categories)
                        )},
                        trailingIcon = { Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")})
                    DropdownMenu(
                        expanded = state.isCategoryDropdownDisplayed,
                        onDismissRequest = { onEvent(StatisticsScreenEvent.DismissCategoryDropdown) }) {
                        DropdownMenuItem(
                            text = { Text(allCategoriesString) },
                            onClick = { onEvent(StatisticsScreenEvent.SetCategory(null)) })
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
                        label = {Text("${state.datePeriodString} ${stringResource(id = R.string.days)}")},
                        trailingIcon = { Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")})
                    DropdownMenu(
                        expanded = state.isDatePeriodDropdownDisplayed,
                        onDismissRequest = { onEvent(StatisticsScreenEvent.DismissPeriodDropdown) }) {
                        DropdownMenuItem(text = { Text("7 ${stringResource(id = R.string.days)}") }, onClick = { onEvent(StatisticsScreenEvent.SetPeriodInDays(7)) })
                        DropdownMenuItem(text = { Text("14 ${stringResource(id = R.string.days)}") }, onClick = { onEvent(StatisticsScreenEvent.SetPeriodInDays(14)) })
                        DropdownMenuItem(text = { Text("30 ${stringResource(id = R.string.days)}") }, onClick = { onEvent(StatisticsScreenEvent.SetPeriodInDays(30)) })
                        DropdownMenuItem(text = { Text("90 ${stringResource(id = R.string.days)}") }, onClick = { onEvent(StatisticsScreenEvent.SetPeriodInDays(90)) })
                    }
                }
            }
            CategoryAmountChart(
                dataSet = state.categoryAmountChartDataSet
            )
            for (data in state.amountListDataSet){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
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
                        label = { Text(
                            state.selectedHabitNameResource?.let { stringResource(id = state.selectedHabitNameResource) }
                                ?: state.selectedHabitName
                        ) },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = ""
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
                dataSet = state.completionTimeDataSet,
                noDataMessage = if(state.isHabitSelected) stringResource(id = R.string.no_data_to_display) else ""
            )
        }
    }
}