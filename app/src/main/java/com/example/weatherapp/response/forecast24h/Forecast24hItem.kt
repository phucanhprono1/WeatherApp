package com.example.weatherapp.response.forecast24h

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "forecast24h_table",indices = [Index(value = ["DateTime"], unique = true)])
data class Forecast24hItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int?= null,
    val DateTime: String,
    val EpochDateTime: Int,
    val HasPrecipitation: Boolean,
    val IconPhrase: String,
    val IsDaylight: Boolean,
    val Link: String,
    val MobileLink: String,
    val PrecipitationProbability: Int,
    @Embedded(prefix = "temperature_")
    val Temperature: Temperature,
    val WeatherIcon: Int
)