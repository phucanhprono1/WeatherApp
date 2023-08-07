package com.example.weatherapp.response.FivedaysForecast

import androidx.room.Embedded

data class Wind(
    @Embedded(prefix = "direction_")
    val Direction: Direction,
    @Embedded(prefix = "speed_")
    val Speed: Speed
)