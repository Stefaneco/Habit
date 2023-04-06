package com.example.habit.di

import com.example.habit.domain.notification.FakeAlarmScheduler
import com.example.habit.domain.notification.alarm.IAlarmScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AlarmModule::class]
)
class FakeAlarmModule {

    @Provides
    @Singleton
    fun provideAlarmScheduler() : IAlarmScheduler {
        return FakeAlarmScheduler()
    }
}