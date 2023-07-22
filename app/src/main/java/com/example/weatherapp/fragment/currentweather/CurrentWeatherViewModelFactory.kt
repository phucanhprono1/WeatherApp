package com.example.weatherapp.fragment.currentweather

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.repository.ForecastRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentWeatherViewModelFactory @Inject constructor(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val forecastRepository: ForecastRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrentWeatherViewModel::class.java)) {
            return CurrentWeatherViewModel(context, lifecycleOwner,forecastRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}