package com.example.weatherapp.response.currentweather

import androidx.room.Embedded

data class Temperature(
    @Embedded(prefix = "Imperial_")
    val Imperial: Imperial,
    @Embedded(prefix = "Metric_")
    val Metric: Metric
)