package com.example.weatherapp.repository

import androidx.lifecycle.LiveData
import com.example.weatherapp.response.FivedaysForecast.DailyForecast
import com.example.weatherapp.weatherunit.currentweather.UnitLocalizedCurrentWeather
import com.example.weatherapp.weatherunit.forecastweather.UnitLocalizedFiveDaysForecastWeather
import org.threeten.bp.LocalDate
import javax.inject.Singleton


interface ForecastRepository {
    suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitLocalizedCurrentWeather>
    fun getWeatherNonLive(metric: Boolean): UnitLocalizedCurrentWeather
    suspend fun getFutureWeatherList(startDate: LocalDate, metric: Boolean): LiveData<out List< UnitLocalizedFiveDaysForecastWeather>>
    fun get5dayForecastNonLive(startDate: LocalDate, metric: Boolean):List<UnitLocalizedFiveDaysForecastWeather>

}