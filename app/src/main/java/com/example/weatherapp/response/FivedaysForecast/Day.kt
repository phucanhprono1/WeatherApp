package com.example.weatherapp.response.FivedaysForecast

data class Day(
    val HasPrecipitation: Boolean,
    val Icon: Int,
    val IconPhrase: String? = "",
    val PrecipitationIntensity: String? = "",
    val PrecipitationType: String? = ""
)