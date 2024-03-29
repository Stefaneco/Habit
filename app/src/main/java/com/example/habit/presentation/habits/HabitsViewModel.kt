package com.example.habit.presentation.habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habit.domain.interactors.CreateHabit
import com.example.habit.domain.interactors.UpdateHabit
import com.example.habit.domain.model.Habit
import com.example.habit.domain.model.HabitCategory
import com.example.habit.domain.repositories.IHabitRepository
import com.example.habit.domain.util.DateTimeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.atTime
import java.lang.Long.max
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val habitRepository: IHabitRepository,
    private val createHabit: CreateHabit,
    private val updateHabit: UpdateHabit
): ViewModel() {

    private val _state = MutableStateFlow(HabitsScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update { it.copy(
                isLoading = false,
                habits = habitRepository.getAllHabits().sortedBy {
                    habit -> habit.start.time }
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
                    if(!isValidHabit(newHabitName, newHabitCategory)) return
                    val startDateTime = newHabitDate.atTime(newHabitTime)
                    val newHabit = Habit(
                        name = newHabitName.trim(),
                        start = startDateTime,
                        nextOccurrence =
                        DateTimeUtil.fromEpochMillis(
                            max(DateTimeUtil.toEpochMillis(
                            DateTimeUtil.now().date.atTime(newHabitTime)
                        ) + DateTimeUtil.DAY_IN_MILLIS * 2,
                            DateTimeUtil.toEpochMillis(startDateTime))),
                        category = HabitCategory(0,newHabitCategory),
                        repetition = newHabitRepetition
                    )
                    viewModelScope.launch {
                        val newHabitId = createHabit(newHabit)
                        val newHabitList = (habits + listOf(newHabit.copy(id = newHabitId)))
                            .sortedBy { it.start.time }

                        _state.update { it.copy(
                            isNewHabitCreatorOpen = false,
                            newHabitName = "",
                            isValidNewHabit = false,
                            habits = newHabitList
                        ) }
                    }
                }
            }
            is HabitsScreenEvent.EditNewHabitName -> {
                _state.update { it.copy(
                    newHabitName = event.name,
                    isValidNewHabit = isValidHabit(event.name, it.newHabitCategory)
                ) }
            }
            is HabitsScreenEvent.EditNewHabitCategory -> {
                _state.update { it.copy(
                    newHabitCategory = event.category,
                    isValidNewHabit = isValidHabit(it.newHabitName, event.category)
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
            is HabitsScreenEvent.EditNewHabitDate -> {
                _state.update { it.copy(
                    newHabitDate = event.date
                ) }
            }
            is HabitsScreenEvent.DeleteHabit -> {
                viewModelScope.launch {
                    habitRepository.deleteHabit(event.habitId)
                }
                val newHabits = _state.value.habits.toMutableList()
                newHabits.removeIf { it.id == event.habitId }
                _state.update { it.copy(
                    habits = newHabits
                ) }
            }
            is HabitsScreenEvent.OpenHabitEditor -> {
                _state.update { it.copy(
                    isHabitEditorOpen = true,
                    editedHabit = event.habit
                ) }
            }
            is HabitsScreenEvent.CloseHabitEditor -> {
                _state.update { it.copy(
                    isHabitEditorOpen = false,
                    editedHabit = null
                ) }
            }
            is HabitsScreenEvent.EditEditedHabitName -> {
                _state.update { it.copy(
                    editedHabit = it.editedHabit?.copy(name = event.name),
                    isValidEditedHabit = isValidHabit(event.name, it.editedHabit?.category?.name ?: "")
                ) }
            }
            is HabitsScreenEvent.EditEditedHabitCategory -> {
                _state.update { it.copy(
                    editedHabit = it.editedHabit?.copy(category = HabitCategory(0,event.category)),
                    isValidEditedHabit = isValidHabit(it.editedHabit?.name ?: "", event.category)
                ) }
            }
            is HabitsScreenEvent.EditEditedHabitTime -> {
                val currentDateTime = _state.value.editedHabit?.start
                    ?: throw NullPointerException("Edited Habit is null")
                val newDateTime = currentDateTime.date.atTime(event.time)
                _state.update { it.copy(
                    editedHabit = it.editedHabit?.copy(
                        start = newDateTime
                    )
                ) }
            }
            is HabitsScreenEvent.SaveEditedHabit -> {
                viewModelScope.launch {
                    _state.value.editedHabit?.let { updateHabit(it) }
                }
                _state.update { it.copy(
                    isValidEditedHabit = false,
                    isHabitEditorOpen = false,
                    editedHabit = null,
                    habits = _state.value.habits.map { habit ->
                        if(habit.id == _state.value.editedHabit?.id) _state.value.editedHabit!!
                        else habit
                    }
                ) }
            }
            else -> {}
        }
    }

    private fun isValidHabit(name: String, categoryName: String) : Boolean {
        return name.isNotBlank() && categoryName.isNotBlank()
    }
}