package com.example.habit.presentation.habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habit.domain.IHabitRepository
import com.example.habit.domain.model.Habit
import com.example.habit.domain.model.HabitHistoryItem
import com.example.habit.domain.util.DateTimeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.atTime
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val habitsRepository: IHabitRepository
): ViewModel() {

    private val _state = MutableStateFlow(HabitsScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update { it.copy(
                isLoading = false,
                habits = habitsRepository.getAllHabits()
            ) }
        }
    }

    fun onEvent(event: HabitsScreenEvent){
        when(event){
            is HabitsScreenEvent.OpenNewHabitCreator -> {
                _state.update { it.copy(
                    isNewHabitCreatorOpen = true
                ) }
            }
            is HabitsScreenEvent.CloseNewHabitCreator -> {
                _state.update { it.copy(
                    isNewHabitCreatorOpen = false
                ) }
            }
            is HabitsScreenEvent.CreateNewHabit -> {
                with(_state.value){
                    if(!isValidHabitName(newHabitName)) return
                    val startDateTime = DateTimeUtil.now().date.atTime(newHabitTime)
                    val newHabit = Habit(
                        name = newHabitName,
                        start = DateTimeUtil.toEpochMillis(startDateTime),
                        nextOccurrence = DateTimeUtil.toEpochMillis(startDateTime) + DateTimeUtil.DAY_IN_MILLIS * 2,
                        category = newHabitCategory,
                        repetition = newHabitRepetition
                    )
                    viewModelScope.launch {
                        val newHabitId = habitsRepository.insertHabit(newHabit)
                        val newHabitList = habits + listOf(newHabit.copy(id = newHabitId))
                        _state.update { it.copy(
                            isNewHabitCreatorOpen = false,
                            newHabitName = "",
                            isValidNewHabit = false,
                            habits = newHabitList
                        ) }
                        val newHabitHistoryItems = mutableListOf(
                            HabitHistoryItem(
                                habitId = newHabitId,
                                dateTimeTimestamp = newHabit.start + DateTimeUtil.DAY_IN_MILLIS
                            ),
                            HabitHistoryItem(
                                habitId = newHabitId,
                                dateTimeTimestamp = newHabit.start + DateTimeUtil.DAY_IN_MILLIS * 2
                            )
                        )
                        if(newHabitTime >= DateTimeUtil.now().time) newHabitHistoryItems.add(
                            HabitHistoryItem(
                                habitId = newHabitId,
                                dateTimeTimestamp = newHabit.start
                            )
                        )
                        habitsRepository.insertHabitHistoryItems(newHabitHistoryItems)
                    }
                }
            }
            is HabitsScreenEvent.EditNewHabitName -> {
                _state.update { it.copy(
                    newHabitName = event.name,
                    isValidNewHabit = isValidHabitName(event.name)
                ) }
            }
            is HabitsScreenEvent.EditNewHabitCategory -> {
                _state.update { it.copy(
                    newHabitCategory = event.category
                ) }
            }
            is HabitsScreenEvent.EditNewHabitRepetition -> {
                _state.update { it.copy(
                    newHabitRepetition = event.repetition
                ) }
            }
            is HabitsScreenEvent.EditNewHabitTime -> {
                _state.update { it.copy(
                    newHabitTime = event.time
                ) }
            }
            else -> {}
        }
    }

    private fun isValidHabitName(name: String) : Boolean {
        return name.isNotBlank()
    }
}