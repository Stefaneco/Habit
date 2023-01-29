package com.example.habit.presentation.core.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.habit.domain.util.DateTimeUtil
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalDatePicker(
    date: LocalDate,
    onSelectionEvent: (LocalDate) -> Unit
){
    val calendarState = rememberSheetState()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(4.dp)
    ) {
        InputChip(
            selected = false,
            onClick = {  },
            label = {Text(text = DateTimeUtil.formatShortDate(date))},
            trailingIcon = {Icon(imageVector = Icons.Filled.DateRange, contentDescription = "")}
        )
    }

    CalendarDialog(state = calendarState, selection = CalendarSelection.Date { javaDate ->
        onSelectionEvent(javaDate.toKotlinLocalDate())
    })
}