package com.example.habit.presentation.day.components

import androidx.compose.runtime.Composable
import com.example.habit.presentation.core.components.BottomCreator
import com.example.habit.presentation.core.components.TimePicker
import com.example.habit.presentation.day.DayScreenEvent
import kotlinx.datetime.LocalTime

@Composable
fun HabitHistoryItemEditor(
    itemId: Long,
    time: LocalTime,
    onEvent: (DayScreenEvent) -> Unit
) {
    BottomCreator(
        buttonText = "Done!",
        buttonOnClick = { onEvent(DayScreenEvent.ChangeHabitHistoryItemState(itemId)) },
        onCloseEvent = { onEvent(DayScreenEvent.CloseItemEditor) }) {
        TimePicker(time = time, onSelectionEvent = { onEvent(DayScreenEvent.EditSelectedItemTime(it))})
    }
}