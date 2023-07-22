package com.example.weatherapp.repository

import androidx.lifecycle.LiveData
import com.example.weatherapp.entity.currentweather.UnitLocalizedCurrentWeather

interface ForecastRepository {
    suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitLocalizedCurrentWeather>
}