package com.example.habit.presentation.core.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = DateTimeUtil.formatDate(date))
        IconButton(onClick = { calendarState.show() }) {
            Icon(imageVector = Icons.Filled.DateRange, contentDescription = "")
        }
    }

    CalendarDialog(state = calendarState, selection = CalendarSelection.Date { javaDate ->
        onSelectionEvent(javaDate.toKotlinLocalDate())
    })
}