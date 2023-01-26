package com.example.habit.presentation.statistics

import com.example.habit.domain.model.HabitCategory
import com.example.habit.domain.util.DateTimeUtil
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus

data class StatisticsScreenState (
    val datePeriodString: String = "14 days",
    val startDate: LocalDate = DateTimeUtil.now().date.minus(DatePeriod(days = 14)),
    val endDate: LocalDate = DateTimeUtil.now().date,
    val isDatePeriodDropdownDisplayed: Boolean = false,

    val categories : List<HabitCategory> = emptyList(),
    val isCategorySelected: Boolean = false,
    val categoryId: Long = 0,
    val categoryName: String = "Category",
    val isCategoryDropdownDisplayed: Boolean = false,
        )

