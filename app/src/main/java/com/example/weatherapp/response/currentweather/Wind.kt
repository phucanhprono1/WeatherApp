package com.example.weatherapp.response.currentweather

import androidx.room.Embedded
import com.example.weatherapp.response.FivedaysForecast.Direction

data class Wind(
    @Embedded(prefix = "direction_")
    val Direction: Direction,
    @Embedded(prefix = "speed_")
    val Speed: Speed
)
