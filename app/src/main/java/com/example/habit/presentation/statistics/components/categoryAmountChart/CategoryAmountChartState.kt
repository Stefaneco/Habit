package com.example.habit.presentation.statistics.components.categoryAmountChart

import com.example.habit.domain.util.DateTimeUtil
import com.example.habit.presentation.statistics.components.categoryAmountChart.model.CategoryAmountChartInfo
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus

data class CategoryAmountChartState(
    val categoryId: Long = 0,
    val category: String = "",
    val dataSet: List<CategoryAmountChartInfo> = emptyList(),
    val startDate: LocalDate = DateTimeUtil.now().date.minus(DatePeriod(days = 14)),
    val endDate: LocalDate = DateTimeUtil.now().date
)