package com.example.weatherapp.response.FivedaysForecast

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "future_weather", indices = [Index(value = ["Date"], unique = true)])
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
    @Embedded(prefix = "wind_")
    val Wind: Wind,
    @Embedded(prefix = "temperature_")
    val Temperature: Temperature
)