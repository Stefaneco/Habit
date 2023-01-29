package com.example.habit.presentation.habits.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.example.habit.R
import com.example.habit.presentation.core.components.BottomCreator
import com.example.habit.presentation.core.components.LocalTimePicker
import com.example.habit.presentation.habits.HabitsScreenEvent
import kotlinx.datetime.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitEditor(
    name: String,
    time: LocalTime,
    category: String,
    isValidHabit: Boolean,
    onEvent: (HabitsScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    BottomCreator(
        buttonText = stringResource(id = R.string.save),
        buttonOnClick = { onEvent(HabitsScreenEvent.SaveEditedHabit) },
        onCloseEvent = { onEvent(HabitsScreenEvent.CloseHabitEditor) },
        isButtonEnabled = isValidHabit,
        modifier = modifier
    ){
        LocalTimePicker(
            time = time,
            onSelectionEvent = { onEvent(HabitsScreenEvent.EditEditedHabitTime(it)) }
        )
        Spacer(modifier = Modifier.padding(4.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            value = name,
            onValueChange = {onEvent(HabitsScreenEvent.EditEditedHabitName(it))},
            label = { Text(stringResource(id = R.string.habit_name)) },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
        )
        Spacer(modifier = Modifier.padding(4.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            value = category,
            onValueChange = {onEvent(HabitsScreenEvent.EditEditedHabitCategory(it))},
            label = { Text(stringResource(id = R.string.category)) },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
        )
    }
}