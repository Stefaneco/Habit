package com.example.habit.presentation.statistics

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(

): ViewModel() {
    private val _state = MutableStateFlow(StatisticsScreenState())
    val state = _state.asStateFlow()

    init {

    }

    fun onEvent(event: StatisticsScreenEvent){
        when(event){
            else -> {}
        }
    }
}