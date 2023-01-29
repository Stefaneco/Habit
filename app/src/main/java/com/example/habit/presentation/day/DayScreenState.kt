package com.example.habit.presentation.day

import com.example.habit.R
import com.example.habit.domain.model.HabitHistoryItem
import com.example.habit.domain.util.DateTimeUtil
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class DayScreenState (
    val habitHistoryItems : List<HabitHistoryItem> = emptyList(),
    val date: LocalDate = DateTimeUtil.now().date,
    val dateString: String = "",
    val dateStringResource : Int? = R.string.today,
    val isLoading: Boolean = true,
    val isHabitItemUpdating : Boolean = false,
    val selectedItemTime : LocalTime = DateTimeUtil.now().time,
    val isItemEditorOpen : Boolean = false,
    val selectedItemId: Long = 0
    )

