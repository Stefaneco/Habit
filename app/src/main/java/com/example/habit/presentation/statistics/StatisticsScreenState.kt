package com.example.habit.presentation.statistics

import com.example.habit.R
import com.example.habit.domain.model.HabitCategory
import com.example.habit.domain.model.dto.HabitName
import com.example.habit.domain.util.DateTimeUtil
import com.example.habit.presentation.statistics.model.NameAndAmountInfo
import kotlinx.datetime.*

data class StatisticsScreenState (
    val datePeriodString: String = "14",
    val startDate: LocalDate = DateTimeUtil.now().date.minus(DatePeriod(days = 14)),
    val endDate: LocalDate = DateTimeUtil.now().date,
    val isDatePeriodDropdownDisplayed: Boolean = false,

    //Categories dropdown
    val categories : List<HabitCategory> = emptyList(),
    val isCategorySelected: Boolean = false,
    val categoryId: Long? = null,
    val categoryName: String? = null,
    val isCategoryDropdownDisplayed: Boolean = false,

    //Habits dropdown
    val habitNames: List<HabitName> = emptyList(),
    val selectedHabitName: String = "",
    val selectedHabitNameResource: Int? = R.string.select_habit,
    val selectedHabitId: Long = 0,
    val isHabitSelected: Boolean = false,
    val isHabitDropdownDisplayed: Boolean = false,
    val isHabitChipEnabled : Boolean = false,

    //Selected data
    val categoryAmountChartDataSet: List<NameAndAmountInfo> = emptyList(),
    val amountListDataSet: List<NameAndAmountInfo> = emptyList(),
    val completionTimeDataSet: List<LocalDateTime> = emptyList(),
    val perfectCompletionTime: LocalTime = LocalTime(hour = 10, minute = 0)
        )

