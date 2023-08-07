package com.example.weatherapp.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.response.forecast24h.Forecast24h
import com.example.weatherapp.response.forecast24h.Forecast24hItem
import com.example.weatherapp.weatherunit.forecast24h.Metric24hForecast
import com.example.weatherapp.weatherunit.forecast24h.UnitLocalized24hForecast
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

@Dao
interface HourlyForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<Forecast24hItem>)
    @Query("select * from forecast24h_table where DateTime >= :startDate")
    fun getHourlyForecastMetric(startDate: LocalDateTime): LiveData< List<Metric24hForecast>>
    @Query("select * from forecast24h_table where DateTime >= :startDate")
    fun getHourlyForecastMetricNonLive(startDate: LocalDateTime): List<Metric24hForecast>
    @Query("select count(id) from forecast24h_table where DateTime >= :startDate")
    fun countHourlyForecast(startDate: LocalDateTime): Int
    @Query("delete from forecast24h_table where DateTime < :firstDateToKeep")
    fun deleteOldEntries(firstDateToKeep: LocalDateTime)
}