package com.example.weatherapp.response.FivedaysForecast

import androidx.room.Embedded

data class Temperature(
    @Embedded(prefix = "maximum_")
    val Maximum: Maximum,
    @Embedded(prefix = "minimum_")
    val Minimum: Minimum
)