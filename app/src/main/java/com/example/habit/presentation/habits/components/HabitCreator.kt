package com.example.habit.presentation.habits.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.example.habit.R
import com.example.habit.presentation.core.components.BottomCreator
import com.example.habit.presentation.core.components.LocalDatePicker
import com.example.habit.presentation.core.components.LocalTimePicker
import com.example.habit.presentation.habits.HabitsScreenEvent
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitCreator(
    name: String,
    time: LocalTime,
    date: LocalDate,
    repetition: String,
    category: String,
    isValidHabit: Boolean,
    onEvent: (HabitsScreenEvent) -> Unit,
    modifier: Modifier = Modifier
){
    BottomCreator(
        buttonText = stringResource(id = R.string.create),
        buttonOnClick = { onEvent(HabitsScreenEvent.CreateNewHabit) },
        onCloseEvent = { onEvent(HabitsScreenEvent.CloseNewHabitCreator) },
        isButtonEnabled = isValidHabit,
        modifier = modifier
    )
    {
        Row(horizontalArrangement = Arrangement.Center) {
            LocalDatePicker(date = date, onSelectionEvent = {onEvent(HabitsScreenEvent.EditNewHabitDate(it))})
            Spacer(modifier = Modifier.padding(4.dp))
            LocalTimePicker(
                time = time,
                onSelectionEvent = { onEvent(HabitsScreenEvent.EditNewHabitTime(it)) }
            )
            Spacer(modifier = Modifier.padding(4.dp))
        }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            value = name,
            onValueChange = {onEvent(HabitsScreenEvent.EditNewHabitName(it))},
            label = {Text(stringResource(id = R.string.habit_name))},
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
        )
        Spacer(modifier = Modifier.padding(4.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            value = category,
            onValueChange = {onEvent(HabitsScreenEvent.EditNewHabitCategory(it))},
            label = {Text(stringResource(id = R.string.category))},
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
        )
    }
}