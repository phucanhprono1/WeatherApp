package com.example.weatherapp.response.currentweather

data class MetricRealFeel(
    val Phrase: String,
    val Unit: String,
    val UnitType: Int,
    val Value: Double
)