package com.example.habit.presentation.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habit.domain.IHabitRepository
import com.example.habit.domain.model.HabitHistoryItem
import com.example.habit.domain.util.DateTimeUtil
import com.example.habit.domain.util.rangeTo
import com.example.habit.presentation.statistics.model.NameAndAmountInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val habitRepository: IHabitRepository
): ViewModel() {
    private val _state = MutableStateFlow(StatisticsScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            updateDataSets()
            _state.update { it.copy(
                categories = habitRepository.getAllCategories()
            ) }
        }
    }

    fun onEvent(event: StatisticsScreenEvent){
        when(event){
            is StatisticsScreenEvent.DismissCategoryDropdown -> {
                _state.update { it.copy(
                    isCategoryDropdownDisplayed = false
                ) }
            }
            is StatisticsScreenEvent.DismissPeriodDropdown -> {
                _state.update { it.copy(
                    isDatePeriodDropdownDisplayed = false
                ) }
            }
            is StatisticsScreenEvent.OpenCategoryDropdown -> {
                _state.update { it.copy(
                    isCategoryDropdownDisplayed = true
                ) }
            }
            is StatisticsScreenEvent.OpenPeriodDropdown -> {
                _state.update { it.copy(
                    isDatePeriodDropdownDisplayed = true
                ) }
            }
            is StatisticsScreenEvent.SetPeriodInDays -> {
                _state.update { it.copy(
                    startDate = DateTimeUtil.now().date.minus(DatePeriod(days = event.days)),
                    datePeriodString = "${event.days} days",
                    isDatePeriodDropdownDisplayed = false
                ) }
                updateDataSets()
            }
            is StatisticsScreenEvent.SetCategory -> {
                if(event.category.id == 0L){
                    _state.update { it.copy(
                        categoryId = event.category.id,
                        categoryName = "Category",
                        isCategorySelected = false,
                        isCategoryDropdownDisplayed = false
                    ) }
                }
                else _state.update { it.copy(
                    categoryId = event.category.id,
                    categoryName = event.category.name,
                    isCategorySelected = true,
                    isCategoryDropdownDisplayed = false
                ) }
                updateDataSets()
            }
            else -> {}
        }
    }

    private fun updateDataSets(){
        viewModelScope.launch {
            val habitItems =
                if(_state.value.categoryId == 0L)
                    habitRepository.getHabitHistoryItems(
                    fromTimestamp = DateTimeUtil.dayStartEpochMillis(_state.value.startDate),
                    toTimestamp = DateTimeUtil.dayEndEpochMillis(_state.value.endDate)
                )
                else
                    habitRepository.getHabitHistoryItems(
                    fromTimestamp = DateTimeUtil.dayStartEpochMillis(_state.value.startDate),
                    toTimestamp = DateTimeUtil.dayEndEpochMillis(_state.value.endDate), categoryId = _state.value.categoryId
                )

            val groupedByDateItems = habitItems.reversed()
                .filter { it.isDone }
                .groupBy { DateTimeUtil.fromEpochMillis(it.dateTimeTimestamp).date }
            val dateMap = mutableMapOf<LocalDate, List<HabitHistoryItem>>()
            for(date in _state.value.startDate.._state.value.endDate){
                dateMap[date] = groupedByDateItems[date] ?: emptyList()
            }
            val chartDataSet = mutableListOf<NameAndAmountInfo>()
            for (group in dateMap){
                chartDataSet.add(NameAndAmountInfo(DateTimeUtil.formatDate(group.key, getYear = false),group.value.size))
            }

            val groupedByNameItems = habitItems
                .filter { it.isDone }
                .groupBy { it.habitName }
            val nameDataSet = mutableListOf<NameAndAmountInfo>()
            for (group in groupedByNameItems){
                nameDataSet.add(NameAndAmountInfo(group.key, group.value.size))
            }

            _state.update { it.copy(
                categoryAmountChartDataSet = chartDataSet,
                amountListDataSet = nameDataSet.sortedByDescending { info -> info.amount }
            ) }
        }
    }
}