package com.example.weatherapp.response.geolocation

import androidx.room.Embedded

data class Elevation(
    @Embedded
    val Imperial: Imperial,
    @Embedded
    val Metric: Metric
)