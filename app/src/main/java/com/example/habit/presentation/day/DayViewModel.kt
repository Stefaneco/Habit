package com.example.habit.presentation.day

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
import kotlinx.datetime.plus
import javax.inject.Inject

@HiltViewModel
class DayViewModel @Inject constructor(
    private val habitRepository: IHabitRepository
): ViewModel() {

    private val _state = MutableStateFlow(DayScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update { it.copy(
                isLoading = false,
                habitHistoryItems = habitRepository.getHabitHistoryItems(
                    fromTimestamp = DateTimeUtil.dayStartEpochMillis(),
                    toTimestamp = DateTimeUtil.dayEndEpochMillis()
                ).sortedBy { item -> item.dateTimeTimestamp }
            ) }
        }
    }

    fun onEvent(event: DayScreenEvent){
        when(event){
            is DayScreenEvent.ChangeHabitHistoryItemState -> {
                val habitHistoryItems = _state.value.habitHistoryItems
                val updatedItem = habitHistoryItems.first { item -> item.id == event.id }.apply {
                    isDone = !isDone
                    doneTimestamp = if(isDone) DateTimeUtil.nowEpochMillis() else null
                }
                _state.update { it.copy(
                    isHabitItemUpdating = true
                ) }
                viewModelScope.launch {
                    habitRepository.upsertHabitHistoryItem(updatedItem)
                    _state.update { it.copy(
                        habitHistoryItems = habitHistoryItems
                            .map {
                                old -> if(old.id == updatedItem.id) updatedItem else old }
                            .sortedBy { item -> item.dateTimeTimestamp },
                        isHabitItemUpdating = false
                    ) }
                }
            }
            is DayScreenEvent.MinusOneDay -> {
                val newDate = _state.value.date.minus(DatePeriod(days = 1))
                viewModelScope.launch {
                    _state.update { it.copy(
                        date = newDate,
                        habitHistoryItems = habitRepository.getHabitHistoryItems(
                            fromTimestamp = DateTimeUtil.dayStartEpochMillis(newDate),
                            toTimestamp = DateTimeUtil.dayEndEpochMillis(newDate)
                        ).sortedBy { item -> item.dateTimeTimestamp },
                        dateString = DateTimeUtil.formatFriendlyDate(newDate)
                    ) }
                }
            }
            is DayScreenEvent.PlusOneDay -> {
                val newDate = _state.value.date.plus(DatePeriod(days = 1))
                viewModelScope.launch {
                    _state.update { it.copy(
                        date = newDate,
                        habitHistoryItems = habitRepository.getHabitHistoryItems(
                            fromTimestamp = DateTimeUtil.dayStartEpochMillis(newDate),
                            toTimestamp = DateTimeUtil.dayEndEpochMillis(newDate)
                        ).sortedBy { item -> item.dateTimeTimestamp },
                        dateString = DateTimeUtil.formatFriendlyDate(newDate)
                    ) }
                }
            }
            is DayScreenEvent.CloseItemEditor -> {
                _state.update { it.copy(
                    isItemEditorOpen = false
                ) }
            }
            is DayScreenEvent.OpenItemEditor -> {
                _state.update { it.copy(
                    isItemEditorOpen = true,
                    selectedItemId = event.itemId,
                    selectedItemTime = DateTimeUtil.now().time
                ) }
            }
            is DayScreenEvent.EditSelectedItemTime -> {
                _state.update { it.copy(
                    selectedItemTime = event.time
                ) }
            }
            else -> {}
        }
    }
}