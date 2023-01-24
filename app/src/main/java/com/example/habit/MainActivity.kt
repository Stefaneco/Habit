package com.example.habit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.habit.domain.work.DailyWorker
import com.example.habit.presentation.Routes
import com.example.habit.presentation.day.DayScreen
import com.example.habit.presentation.day.DayScreenEvent
import com.example.habit.presentation.day.DayViewModel
import com.example.habit.presentation.habits.HabitsScreen
import com.example.habit.presentation.habits.HabitsScreenEvent
import com.example.habit.presentation.habits.HabitsViewModel
import com.example.habit.presentation.theme.HabitTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dailyRequest = PeriodicWorkRequestBuilder<DailyWorker>(
            1, TimeUnit.DAYS
        ).build()

        WorkManager
            .getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                "dailyWorker",
                ExistingPeriodicWorkPolicy.KEEP,
                dailyRequest
            )
        setContent {
            HabitTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HabitRoot()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitRoot(){
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.padding(4.dp)
    ) { padding ->
        NavHost(
            modifier = Modifier.padding(padding),
            navController = navController,
            startDestination = Routes.HABITS) {
            composable(route = Routes.HABITS){
                val viewModel = hiltViewModel<HabitsViewModel>()
                val state by viewModel.state.collectAsState()
                HabitsScreen(state = state, onEvent = { event ->
                    when(event){
                        is HabitsScreenEvent.NavigateToDayScreen -> {
                            navController.navigate(Routes.DAY){
                                popUpTo(Routes.HABITS){
                                    inclusive = true
                                }
                            }
                        }
                        else -> {viewModel.onEvent(event)}
                    }
                })
            }
            composable(Routes.DAY){
                val viewModel = hiltViewModel<DayViewModel>()
                val state by viewModel.state.collectAsState()
                DayScreen(state = state, onEvent = { event ->
                    when(event){
                        is DayScreenEvent.NavigateToHabitsScreen -> {
                            navController.navigate(Routes.HABITS){
                                popUpTo(Routes.DAY) {
                                    inclusive = true
                                }
                            }
                        }
                        else -> { viewModel.onEvent(event) }
                    }
                })
            }
        }
    }
}
