package com.example.habit.presentation.habits.components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.habit.domain.model.Habit
import com.example.habit.domain.util.DateTimeUtil
import com.example.habit.presentation.habits.HabitsScreenEvent
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
fun HabitCard(
    habit: Habit,
    onEvent: (HabitsScreenEvent) -> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }
    val maxOffsetX = 250f
    Box(){
        Card(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.height(70.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onError,
                contentColor = MaterialTheme.colorScheme.error
            )
        ){
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                    //.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(modifier = Modifier
                    .fillMaxHeight()
                    .width(80.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically){
                    IconButton(onClick = { onEvent(HabitsScreenEvent.DeleteHabit(habit.id)) }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier.size(size = (24* min(1f,offsetX/maxOffsetX)).dp)
                        )
                    }
                }
            }
        }
        Card(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .offset { -IntOffset(offsetX.roundToInt(), 0) }
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        println(offsetX)
                        if (offsetX - delta > 0) offsetX -= delta
                    },
                    onDragStopped = {
                        if (offsetX >= maxOffsetX - 50) offsetX = maxOffsetX
                        else offsetX = 0f
                    }
                )
                .height(70.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(0.7f)
                ) {
                    Text(text = habit.name, style = MaterialTheme.typography.headlineSmall)
                    Text(text = habit.category, style = MaterialTheme.typography.bodySmall)

                }
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(text = DateTimeUtil.formatTime(start = habit.start))
                    Spacer(modifier = Modifier.padding(4.dp))
                    Icon(imageVector = Icons.Filled.Schedule, contentDescription = "")
                }
            }
        }


    }

}