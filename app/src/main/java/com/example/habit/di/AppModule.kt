package com.example.habit.di

import com.example.habit.data.room.AppDatabase
import com.example.habit.domain.interactors.CreateHabit
import com.example.habit.domain.interactors.UpdateHabit
import com.example.habit.domain.interactors.UpdateHabitsNextOccurrences
import com.example.habit.domain.interactors.UpsertHabitHistoryItems
import com.example.habit.domain.notification.alarm.IAlarmScheduler
import com.example.habit.domain.repositories.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

/*    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context) : AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java, "habitDb"
        ).build()
    }*/

    @Provides
    @Singleton
    fun provideHabitRepository(db: AppDatabase) : IHabitRepository {
        return HabitRepository(db)
    }

    @Provides
    @Singleton
    fun provideHabitHistoryRepository(db: AppDatabase) : IHabitHistoryRepository {
        return HabitHistoryRepository(db)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(db: AppDatabase) : ICategoryRepository {
        return CategoryRepository(db)
    }

    @Provides
    @Singleton
    fun provideCreateHabitInteractor(
        habitRepository: IHabitRepository,
        categoryRepository: ICategoryRepository,
        upsertHabitHistoryItems: UpsertHabitHistoryItems) : CreateHabit {
        return CreateHabit(habitRepository, categoryRepository, upsertHabitHistoryItems)
    }

    @Provides
    @Singleton
    fun provideUpsertHabitHistoryItemsInteractor(
        habitHistoryRepository: IHabitHistoryRepository,
        alarmScheduler: IAlarmScheduler) : UpsertHabitHistoryItems {
        return UpsertHabitHistoryItems(habitHistoryRepository,alarmScheduler)
    }

    @Provides
    @Singleton
    fun provideUpdateHabitInteractor(
        habitRepository: IHabitRepository,
        habitHistoryRepository: IHabitHistoryRepository,
        categoryRepository: ICategoryRepository,
        upsertHabitHistoryItems: UpsertHabitHistoryItems
    ) : UpdateHabit {
        return UpdateHabit(
            habitRepository,
            habitHistoryRepository,
            categoryRepository,
            upsertHabitHistoryItems
        )
    }

    @Provides
    @Singleton
    fun provideUpdateHabitsNextOccurrences(
        habitRepository: IHabitRepository,
        upsertHabitHistoryItems: UpsertHabitHistoryItems
    ) : UpdateHabitsNextOccurrences {
        return UpdateHabitsNextOccurrences(habitRepository, upsertHabitHistoryItems)
    }
}