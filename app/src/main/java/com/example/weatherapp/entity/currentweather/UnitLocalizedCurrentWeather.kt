package com.example.weatherapp.entity.currentweather


interface UnitLocalizedCurrentWeather {
    val EpochTime: Long
    val HasPrecipitation: Boolean
    val IsDayTime: Boolean
    val Link: String
    val LocalObservationDateTime: String
    val MobileLink: String
//    val PrecipitationType: Any
    val Temperature: Double
    val Unit: String
    val WeatherIcon: Int
    val WeatherText: String
}