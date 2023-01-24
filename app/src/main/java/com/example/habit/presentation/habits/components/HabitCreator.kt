package com.example.habit.presentation.habits.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.habit.domain.util.DateTimeUtil
import com.example.habit.presentation.habits.HabitsScreenEvent
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaLocalTime
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitCreator(
    name: String,
    time: LocalTime,
    date: LocalDateTime,
    repetition: String,
    category: String,
    isValidHabit: Boolean,
    onEvent: (HabitsScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var offsetY by remember { mutableStateOf(0f) }

    Column(
        modifier = Modifier
            .offset { IntOffset(0, offsetY.roundToInt()) }
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { delta ->
                    println(offsetY)
                    if (offsetY + delta > 0) offsetY += delta
                },
                onDragStopped = {
                    if (offsetY > 250) onEvent(HabitsScreenEvent.CloseNewHabitCreator)
                    else offsetY = 0f
                }
            )
            .fillMaxSize()
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ){
        Card(
            modifier = modifier.fillMaxWidth(1f),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 12.dp
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(4.dp)
            ){
                Spacer(modifier = Modifier.padding(4.dp))
                Box(modifier = Modifier
                    .width(50.dp)
                    .height(4.dp)
                    .background(
                        shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ))
                Spacer(modifier = Modifier.padding(4.dp))
                HabitTimePicker(time = time, onEvent = onEvent)
                Spacer(modifier = Modifier.padding(4.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    value = name,
                    onValueChange = {onEvent(HabitsScreenEvent.EditNewHabitName(it))},
                    label = {Text("Name")})
                Spacer(modifier = Modifier.padding(4.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    value = category,
                    onValueChange = {onEvent(HabitsScreenEvent.EditNewHabitCategory(it))},
                    label = {Text("Category")})
                Spacer(modifier = Modifier.padding(4.dp))
                Button(
                    enabled = isValidHabit,
                    onClick = { onEvent(HabitsScreenEvent.CreateNewHabit) }) {
                    Text("Create")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitTimePicker(
    time: LocalTime,
    onEvent: (HabitsScreenEvent) -> Unit
){
    val clockState = rememberSheetState()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = DateTimeUtil.formatTime(time))
        IconButton(onClick = { clockState.show() }) {
            Icon(imageVector = Icons.Filled.Schedule, contentDescription = "")
        }
    }
    
    ClockDialog(
        state = clockState,
        config = ClockConfig(defaultTime = time.toJavaLocalTime(), is24HourFormat = true),
        selection = ClockSelection.HoursMinutes{ hours, minutes ->
        onEvent(HabitsScreenEvent.EditNewHabitTime(LocalTime(hours,minutes)))
    })
}

@Composable
fun HabitDatePicker(
    date: LocalDateTime
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = DateTimeUtil.formatDate(date))
        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Filled.DateRange, contentDescription = "")
        }
    }
}