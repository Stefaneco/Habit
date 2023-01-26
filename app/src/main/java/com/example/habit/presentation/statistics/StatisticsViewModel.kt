package com.example.habit.presentation.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habit.domain.IHabitRepository
import com.example.habit.domain.util.DateTimeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.minus
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    habitRepository: IHabitRepository
): ViewModel() {
    private val _state = MutableStateFlow(StatisticsScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val categories = habitRepository.getAllCategories()
            _state.update { it.copy(
                categories = categories
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
            }
            else -> {}
        }
    }
}