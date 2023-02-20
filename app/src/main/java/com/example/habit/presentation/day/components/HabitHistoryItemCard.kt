package com.example.habit.presentation.day.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.habit.domain.model.HabitHistoryItem
import com.example.habit.domain.util.DateTimeUtil
import com.example.habit.presentation.day.DayScreenEvent

@Composable
fun HabitHistoryItemCard(
    habitHistoryItem: HabitHistoryItem,
    onEvent: (DayScreenEvent) -> Unit
) {
    Card(shape = MaterialTheme.shapes.medium) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp)
                .pointerInput(habitHistoryItem.id) {
                    detectTapGestures(
                        onLongPress = { onEvent(DayScreenEvent.OpenItemEditor(habitHistoryItem.id)) }
                    )
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(0.7f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = habitHistoryItem.isDone, onCheckedChange = {
                    onEvent(DayScreenEvent.ChangeHabitHistoryItemState(habitHistoryItem.id))
                })
                Text(text = habitHistoryItem.habitName, style = MaterialTheme.typography.headlineSmall)
            }
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(text = DateTimeUtil.formatTime(
                    if(habitHistoryItem.isDone)
                        habitHistoryItem.doneDateTime!!
                    else habitHistoryItem.dateTime
                ))
                Spacer(modifier = Modifier.padding(4.dp))
                Icon(imageVector = Icons.Filled.Schedule, contentDescription = "")
            }
        }
    }
}
