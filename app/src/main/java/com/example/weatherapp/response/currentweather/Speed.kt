package com.example.weatherapp.response.currentweather

import androidx.room.Embedded

data class Speed(
    @Embedded(prefix = "metric_")
    val Metric: Metric,
    @Embedded(prefix = "imperial_")
    val Imperial: Imperial
)
