package com.example.weatherapp.response.FivedaysForecast

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "future_weather", indices = [Index(value = ["Date"], unique = false)])
data class DailyForecast(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val Date: String,
    @Embedded(prefix = "day_")
    val Day: Day,
    val EpochDate: Int,
    val Link: String,
    val MobileLink: String,
    @Embedded(prefix = "night_")
    val Night: Night,
    @Embedded(prefix = "temperature_")
    val Temperature: Temperature
){
    var locationKey: String ?= null
    var timezone: String ?= null
}