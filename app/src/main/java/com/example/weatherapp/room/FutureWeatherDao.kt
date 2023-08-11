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
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZonedDateTime

@Dao
interface FutureWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(futureWeatherEntries: List<DailyForecast>)
    @Query("SELECT COUNT(id) FROM future_weather WHERE locationKey = :locationKey")
    fun countEntriesForLocationKey(locationKey: String): Int
    @Query("select * from future_weather where date(datetime(Date)) >= date(datetime(:startDate))")
    fun getFutureWeatherMetric(startDate: LocalDateTime): LiveData<List<MetricFiveDaysForecastWeather>>
    @Query("select * from future_weather where date(datetime(Date)) >= date(datetime(:startDate)) and locationKey= :locationKey")
    fun getFutureWeatherMetricByLocationKey(startDate: LocalDateTime, locationKey:String): LiveData<List<MetricFiveDaysForecastWeather>>
    @Query("select * from future_weather where date(datetime(Date)) >= date(datetime(:startDate)) and locationKey= :locationKey")
    fun getFutureWeatherMetricByLocationKeyNonLive(startDate: LocalDateTime, locationKey:String): List<MetricFiveDaysForecastWeather>
    @Query("select * from future_weather where date(datetime(Date)) >= date(datetime(:startDate))")
    fun getFutureWeatherMetricNonLive(startDate: LocalDateTime): List<MetricFiveDaysForecastWeather>
    @Query("select count(id) from future_weather where date(datetime(Date)) >= date(datetime(:startDate))")
    fun countFutureWeather(startDate: LocalDateTime): Int
    @Query("delete from future_weather where date(datetime(Date)) < date(datetime(:firstDateToKeep))")
    fun deleteOldEntries(firstDateToKeep: LocalDateTime)
    @Query("delete from future_weather where date(datetime(Date)) < date(datetime(:firstDateToKeep)) and locationKey= :locationKey")
    fun deleteOldEntriesByLocationKey(firstDateToKeep: ZonedDateTime, locationKey:String)

}