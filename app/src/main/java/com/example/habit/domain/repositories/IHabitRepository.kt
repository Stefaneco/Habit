package com.example.habit.domain.repositories

import com.example.habit.domain.model.Habit
import com.example.habit.domain.model.dto.HabitName

interface IHabitRepository {
    suspend fun getAllHabits() : List<Habit>
    suspend fun insertHabit(habit: Habit) : Long
    suspend fun updateHabits(habits: List<Habit>)
    suspend fun updateHabit(habit: Habit)
    suspend fun deleteHabit(habitId: Long, deletePlanned: Boolean = true, deleteHistory: Boolean = false)
    suspend fun getHabitsNamesByCategory(categoryId: Long) : List<HabitName>
    suspend fun getHabit(habitId: Long) : Habit
}