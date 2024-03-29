package com.example.habit.presentation.day

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habit.domain.interactors.UpsertHabitHistoryItems
import com.example.habit.domain.repositories.IHabitHistoryRepository
import com.example.habit.domain.repositories.IHabitRepository
import com.example.habit.domain.util.DateTimeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.atTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import javax.inject.Inject

@HiltViewModel
class DayViewModel @Inject constructor(
    private val habitRepository: IHabitRepository,
    private val habitHistoryRepository: IHabitHistoryRepository,
    private val upsertHabitHistoryItems: UpsertHabitHistoryItems
): ViewModel() {

    private val _state = MutableStateFlow(DayScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val earliestItemDate = habitHistoryRepository.getEarliestHabitHistoryItemDate()
            val latestItemDate = habitHistoryRepository.getLatestHabitHistoryItemDate()
            _state.update { it.copy(
                isLoading = false,
                habitHistoryItems = habitHistoryRepository.getHabitHistoryItems(
                    fromTimestamp = DateTimeUtil.dayStartEpochMillis(),
                    toTimestamp = DateTimeUtil.dayEndEpochMillis()
                ).sortedBy { item -> item.dateTime },
                earliestItemDate = earliestItemDate,
                isPriorDataAvailable = earliestItemDate < it.date,
                latestItemDate = latestItemDate,
                isFutureDataAvailable = latestItemDate > it.date
            ) }
            Log.i("DayViewModel", "EarliestDate: $earliestItemDate")
            Log.i("DayViewModel", "LatestDate: $latestItemDate")
        }
    }

    fun onEvent(event: DayScreenEvent){
        when(event){
            is DayScreenEvent.ChangeHabitHistoryItemState -> {
                val habitHistoryItems = _state.value.habitHistoryItems
                val item = habitHistoryItems.first { item -> item.id == event.id }

                if(_state.value.date != DateTimeUtil.now().date && !item.isDone){
                    _state.update { it.copy(
                        isItemEditorOpen = true,
                        selectedItemId = event.id,
                        selectedItemTime = DateTimeUtil.now().time
                    ) }
                    return
                }

                val updatedItem = item.apply {
                    isDone = !isDone
                    doneDateTime = if(isDone) DateTimeUtil.now() else null
                }
                _state.update { it.copy(
                    isHabitItemUpdating = true
                ) }
                viewModelScope.launch {
                    upsertHabitHistoryItems(updatedItem)
                    _state.update { it.copy(
                        habitHistoryItems = habitHistoryItems
                            .map {
                                old -> if(old.id == updatedItem.id) updatedItem else old }
                            .sortedBy { item -> item.dateTime },
                        isHabitItemUpdating = false
                    ) }
                }
            }
            is DayScreenEvent.MinusOneDay -> {
                val newDate = _state.value.date.minus(DatePeriod(days = 1))
                val dateResource = DateTimeUtil.formatFriendlyDate(newDate)
                viewModelScope.launch {
                    _state.update { it.copy(
                        date = newDate,
                        habitHistoryItems = habitHistoryRepository.getHabitHistoryItems(
                            fromTimestamp = DateTimeUtil.dayStartEpochMillis(newDate),
                            toTimestamp = DateTimeUtil.dayEndEpochMillis(newDate)
                        ).sortedBy { item -> item.dateTime },
                        dateString = dateResource.default,
                        dateStringResource = dateResource.resource,
                        isItemEditorOpen = false,
                        isPriorDataAvailable = it.earliestItemDate < newDate,
                        isFutureDataAvailable = it.latestItemDate > newDate
                    ) }
                }
            }
            is DayScreenEvent.PlusOneDay -> {
                val newDate = _state.value.date.plus(DatePeriod(days = 1))
                val dateResource = DateTimeUtil.formatFriendlyDate(newDate)
                viewModelScope.launch {
                    _state.update { it.copy(
                        date = newDate,
                        habitHistoryItems = habitHistoryRepository.getHabitHistoryItems(
                            fromTimestamp = DateTimeUtil.dayStartEpochMillis(newDate),
                            toTimestamp = DateTimeUtil.dayEndEpochMillis(newDate)
                        ).sortedBy { item -> item.dateTime },
                        dateString = dateResource.default,
                        dateStringResource = dateResource.resource,
                        isItemEditorOpen = false,
                        isPriorDataAvailable = it.earliestItemDate < newDate,
                        isFutureDataAvailable = it.latestItemDate > newDate
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
            is DayScreenEvent.ChangeSelectedHabitHistoryItemState -> {
                val habitHistoryItems = _state.value.habitHistoryItems
                val updatedItem = habitHistoryItems.first { item -> item.id == _state.value.selectedItemId }.apply {
                    isDone = true
                    doneDateTime = dateTime.date.atTime(_state.value.selectedItemTime)
                }
                _state.update { it.copy(
                    isHabitItemUpdating = true,
                    isItemEditorOpen = false
                ) }
                viewModelScope.launch {
                    upsertHabitHistoryItems(updatedItem)
                    _state.update { it.copy(
                        habitHistoryItems = habitHistoryItems
                            .map {
                                    old -> if(old.id == updatedItem.id) updatedItem else old }
                            .sortedBy { item -> item.dateTime },
                        isHabitItemUpdating = false
                    ) }
                }
            }
            else -> {}
        }
    }
}