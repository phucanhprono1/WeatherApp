package com.example.weatherapp.weatherunit.forecastweather

interface UnitLocalizedFiveDaysForecastWeather {
    val date: String
    val maxTemperature: Double
    val minTemperature: Double
    val icon_day: Int
    val iconPhrase_day: String
    val icon_night: Int
    val iconPhrase_night: String
}