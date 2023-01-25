package com.example.habit.presentation.statistics.components.categoryAmountChart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habit.domain.IHabitRepository
import com.example.habit.domain.model.HabitHistoryItem
import com.example.habit.domain.util.DateTimeUtil
import com.example.habit.domain.util.rangeTo
import com.example.habit.presentation.statistics.components.categoryAmountChart.model.CategoryAmountChartInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import javax.inject.Inject

@HiltViewModel
class CategoryAmountChartViewModel @Inject constructor(
    habitRepository: IHabitRepository
) : ViewModel() {
    private val _state = MutableStateFlow(CategoryAmountChartState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val habitItems = habitRepository.getHabitHistoryItems(
                fromTimestamp = DateTimeUtil.dayStartEpochMillis(_state.value.startDate),
                toTimestamp = DateTimeUtil.dayEndEpochMillis(_state.value.endDate)
            )
            val groupedItems = habitItems.reversed()
                .filter { it.isDone }
                .groupBy { DateTimeUtil.fromEpochMillis(it.dateTimeTimestamp).date }
            val dateMap = mutableMapOf<LocalDate, List<HabitHistoryItem>>()
            for(date in _state.value.startDate.._state.value.endDate){
                dateMap[date] = groupedItems[date] ?: emptyList()
            }
            val chartDataSet = mutableListOf<CategoryAmountChartInfo>()
            for (group in dateMap){
                chartDataSet.add(CategoryAmountChartInfo(DateTimeUtil.formatDate(group.key, getYear = false),group.value.size))
            }
            _state.update { it.copy(
                dataSet = chartDataSet
            ) }
        }
    }
}