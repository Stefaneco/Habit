package com.example.habit.presentation.day.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.habit.R
import com.example.habit.presentation.core.components.BottomCreator
import com.example.habit.presentation.core.components.LocalTimePicker
import com.example.habit.presentation.day.DayScreenEvent
import kotlinx.datetime.LocalTime

@Composable
fun HabitHistoryItemEditor(
    itemId: Long,
    time: LocalTime,
    onEvent: (DayScreenEvent) -> Unit
) {
    BottomCreator(
        buttonText = stringResource(id = R.string.done),
        buttonOnClick = { onEvent(DayScreenEvent.ChangeSelectedHabitHistoryItemState) },
        onCloseEvent = { onEvent(DayScreenEvent.CloseItemEditor) }) {
        LocalTimePicker(time = time, onSelectionEvent = { onEvent(DayScreenEvent.EditSelectedItemTime(it))})
    }
}