package com.example.habit.presentation.habits.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.habit.domain.model.Habit
import com.example.habit.domain.util.DateTimeUtil

@Composable
fun HabitCard(
    habit: Habit
) {
   Card(shape = MaterialTheme.shapes.medium) {
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