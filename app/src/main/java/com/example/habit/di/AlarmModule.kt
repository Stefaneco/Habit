package com.example.habit.di

import android.content.Context
import com.example.habit.domain.notification.alarm.AndroidAlarmScheduler
import com.example.habit.domain.notification.alarm.IAlarmScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AlarmModule {

    @Provides
    @Singleton
    fun provideAlarmScheduler(@ApplicationContext appContext: Context) : IAlarmScheduler {
        return AndroidAlarmScheduler(appContext)
    }
}