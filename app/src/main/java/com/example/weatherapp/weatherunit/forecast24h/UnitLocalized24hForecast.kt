package com.example.weatherapp.weatherunit.forecast24h

interface UnitLocalized24hForecast {
    val DateTime: String
    val EpochDateTime: Int
    val temperature_value: Double
    val temperature_unit: String
    val WeatherIcon: Int
    val IconPhrase: String
    val WindSpeed: Double?
//    val WindDirection: String
    val WindUnit: String?
}