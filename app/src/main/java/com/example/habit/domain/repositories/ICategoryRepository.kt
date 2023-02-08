package com.example.habit.domain.repositories

import com.example.habit.domain.model.HabitCategory

interface ICategoryRepository {
    suspend fun getAllCategories() : List<HabitCategory>
    suspend fun isCategoryNameInDb(name: String) : Boolean
    suspend fun insertCategory(category: HabitCategory) : Long
    suspend fun getCategoryByName(name: String) : HabitCategory
}