package com.example.weatherapp.response.FivedaysForecast

import androidx.room.Embedded

data class Day(
    val HasPrecipitation: Boolean,
    val Icon: Int,
    val IconPhrase: String? = "",
    val PrecipitationIntensity: String? = "",
    val PrecipitationType: String? = "",
    @Embedded(prefix = "wind_")
    val Wind: Wind,
)