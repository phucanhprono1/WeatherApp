package com.example.weatherapp.response.currentweather

data class CurrentWeatherResponseItem(
    val EpochTime: Int,
    val HasPrecipitation: Boolean,
    val IsDayTime: Boolean,
    val Link: String,
    val LocalObservationDateTime: String,
    val MobileLink: String,
    val PrecipitationType: Any,
    val Temperature: Temperature,
    val WeatherIcon: Int,
    val WeatherText: String
)