package com.example.weatherapp.response.currentweather

import androidx.room.Embedded


data class Pressure(
    @Embedded(prefix = "Imperial_")
    val Imperial: Imperial,
    @Embedded(prefix = "Metric_")
    val Metric: Metric
)