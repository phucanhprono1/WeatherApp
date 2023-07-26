package com.example.weatherapp.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.weatherunit.currentweather.ImperialCurrentWeather
import com.example.weatherapp.weatherunit.currentweather.MetricCurrentWeather
import com.example.weatherapp.response.currentweather.CURRENT_WEATHER_ID
import com.example.weatherapp.response.currentweather.CurrentWeatherResponseItem

@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weatherEntry: CurrentWeatherResponseItem)
    @Query("select * from current_weather where id = $CURRENT_WEATHER_ID")
    fun getWeatherMetric(): LiveData<MetricCurrentWeather>
    @Query("select * from current_weather where id = $CURRENT_WEATHER_ID")
    fun getWeatherImperial(): LiveData<ImperialCurrentWeather>
    @Query("select * from current_weather where id = $CURRENT_WEATHER_ID")
    fun getWeatherMetricNonLive(): MetricCurrentWeather
    @Query("select * from current_weather where id = $CURRENT_WEATHER_ID")
    fun getWeatherImperialNonLive(): ImperialCurrentWeather
}