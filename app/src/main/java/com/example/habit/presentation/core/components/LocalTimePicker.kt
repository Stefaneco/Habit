package com.example.habit.presentation.core.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.habit.domain.util.DateTimeUtil
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaLocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalTimePicker(
    time: LocalTime,
    onSelectionEvent: (LocalTime) -> Unit
){
    val clockState = rememberSheetState()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = DateTimeUtil.formatTime(time), modifier = Modifier.padding(start = 12.dp))
        IconButton(onClick = { clockState.show() }) {
            Icon(imageVector = Icons.Filled.Schedule, contentDescription = "")
        }
    }

    ClockDialog(
        state = clockState,
        config = ClockConfig(defaultTime = time.toJavaLocalTime(), is24HourFormat = true),
        selection = ClockSelection.HoursMinutes{ hours, minutes ->
            onSelectionEvent(LocalTime(hours,minutes))
        })
}