package com.example.weatherapp.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.response.FivedaysForecast.DailyForecast
import com.example.weatherapp.weatherunit.forecastweather.MetricFiveDaysForecastWeather
import com.example.weatherapp.weatherunit.forecastweather.UnitLocalizedFiveDaysForecastWeather
import org.threeten.bp.LocalDate

@Dao
interface FutureWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(futureWeatherEntries: List<DailyForecast>)
    @Query("select * from future_weather where date(Date) >= date(:startDate)")
    fun getFutureWeatherMetric(startDate: LocalDate): LiveData<List<MetricFiveDaysForecastWeather>>
    @Query("select * from future_weather where date(Date) >= date(:startDate)")
    fun getFutureWeatherMetricNonLive(startDate: LocalDate): List<MetricFiveDaysForecastWeather>
    @Query("select count(id) from future_weather where date(Date) >= date(:startDate)")
    fun countFutureWeather(startDate: LocalDate): Int
    @Query("delete from future_weather where date(date) < date(:firstDateToKeep)")
    fun deleteOldEntries(firstDateToKeep: LocalDate)
}