package com.example.habit.domain.repositories

import com.example.habit.data.room.AppDatabase
import com.example.habit.domain.model.HabitCategory

class CategoryRepository(
    db: AppDatabase,
) : ICategoryRepository {
    private val categoryDao = db.habitCategoryDao()

    override suspend fun getAllCategories(): List<HabitCategory> {
        return categoryDao.getAllCategories().map { HabitCategory.fromHabitCategoryEntity(it) }
    }

    override suspend fun isCategoryNameInDb(name: String): Boolean {
        return categoryDao.isCategoryNameInDb(name)
    }

    override suspend fun insertCategory(category: HabitCategory): Long {
        return categoryDao.insertHabitCategory(HabitCategory.toHabitCategoryEntity(category))
    }

    override suspend fun getCategoryByName(name: String): HabitCategory {
        return HabitCategory.fromHabitCategoryEntity(categoryDao.getHabitCategoryByName(name))
    }
}