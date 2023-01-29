package com.example.habit.presentation.core.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
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
        modifier = Modifier.padding(4.dp)
    ) {
        InputChip(
            selected = false,
            onClick = {  },
            label = {Text(text = DateTimeUtil.formatTime(time))},
            trailingIcon = {Icon(imageVector = Icons.Filled.Schedule, contentDescription = "")}
        )
    }

    ClockDialog(
        state = clockState,
        config = ClockConfig(defaultTime = time.toJavaLocalTime(), is24HourFormat = true),
        selection = ClockSelection.HoursMinutes{ hours, minutes ->
            onSelectionEvent(LocalTime(hours,minutes))
        })
}