package com.example.habit.di

import android.content.Context
import androidx.room.Room
import com.example.habit.data.room.AppDatabase
import com.example.habit.domain.HabitRepository
import com.example.habit.domain.IHabitRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context) : AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java, "habitDb"
        ).build()
    }

    @Provides
    @Singleton
    fun provideHabitRepository(db: AppDatabase) : IHabitRepository {
        return HabitRepository(db)
    }
}