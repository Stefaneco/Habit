package com.example.habit.domain.model

import com.example.habit.data.room.entities.HabitCategoryEntity

data class HabitCategory(
    var id: Long,
    val name: String
) {
    companion object {
        fun fromHabitCategoryEntity(habitCategoryEntity: HabitCategoryEntity) : HabitCategory {
            return HabitCategory(
                id = habitCategoryEntity.id,
                name = habitCategoryEntity.name
            )
        }

        fun toHabitCategoryEntity(habitCategory: HabitCategory): HabitCategoryEntity {
            return HabitCategoryEntity(
                id = habitCategory.id,
                name = habitCategory.name
            )
        }
    }
}
