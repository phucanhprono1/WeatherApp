package com.example.weatherapp.fragment.currentweather

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.repository.ForecastRepository
import javax.inject.Inject
import javax.inject.Singleton

//@Singleton
class CurrentWeatherViewModelFactory @Inject constructor(
    private val applicationContext: Context, // Use the applicationContext instead of context
    private val lifecycleOwner: LifecycleOwner,
    private val forecastRepository: ForecastRepository,

) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrentWeatherViewModel::class.java)) {
            return CurrentWeatherViewModel(applicationContext, lifecycleOwner,forecastRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}