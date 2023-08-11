package com.example.weatherapp.repository

import androidx.lifecycle.LiveData
import com.example.weatherapp.weatherunit.currentweather.UnitLocalizedCurrentWeather
import com.example.weatherapp.weatherunit.forecast24h.UnitLocalized24hForecast
import com.example.weatherapp.weatherunit.forecastweather.UnitLocalizedFiveDaysForecastWeather
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime


interface ForecastRepository {
    suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitLocalizedCurrentWeather>
    suspend fun getCurrentWeatherByLocationKey(locationKey: String,metric: Boolean): LiveData<out UnitLocalizedCurrentWeather>
    fun getWeatherNonLive(metric: Boolean): UnitLocalizedCurrentWeather
    fun getWeatherNonLiveByLocationKey(locationKey: String,metric: Boolean): UnitLocalizedCurrentWeather
    suspend fun getFutureWeatherList(startDate: LocalDateTime, metric: Boolean): LiveData<out List< UnitLocalizedFiveDaysForecastWeather>>
    fun get5dayForecastNonLive(startDate: LocalDateTime, metric: Boolean):List<UnitLocalizedFiveDaysForecastWeather>
    suspend fun getHourlyForecastList(startDate: LocalDateTime, metric: Boolean): LiveData<out List<UnitLocalized24hForecast>>
    fun getHourlyForecastNonLive(startDate: LocalDateTime, metric: Boolean):List<UnitLocalized24hForecast>

    suspend fun getFutureWeatherListByLocationKey(startDate: LocalDateTime, locationKey: String, metric: Boolean): LiveData<out List< UnitLocalizedFiveDaysForecastWeather>>
    fun get5dayForecastNonLiveByLocationKey(startDate: LocalDateTime, locationKey: String, metric: Boolean):List<UnitLocalizedFiveDaysForecastWeather>
    suspend fun getHourlyForecastListByLocationKey(startDate: LocalDateTime, locationKey: String, metric: Boolean): LiveData<out List<UnitLocalized24hForecast>>
    fun getHourlyForecastNonLiveByLocationKey(startDate: LocalDateTime, locationKey: String, metric: Boolean):List<UnitLocalized24hForecast>
}