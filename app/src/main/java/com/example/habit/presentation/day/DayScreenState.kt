package com.example.habit.presentation.day

import com.example.habit.domain.model.HabitHistoryItem
import com.example.habit.domain.util.DateTimeUtil
import kotlinx.datetime.LocalDate

data class DayScreenState (
    val habitHistoryItems : List<HabitHistoryItem> = emptyList(),
    val date: LocalDate = DateTimeUtil.now().date,
    val dateString: String = "Today",
    val isLoading: Boolean = true,
    val isHabitItemUpdating : Boolean = false
    )

