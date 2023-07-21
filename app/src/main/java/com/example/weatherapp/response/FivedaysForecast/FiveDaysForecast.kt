package com.example.weatherapp.response.FivedaysForecast

data class FiveDaysForecast(
    val DailyForecasts: List<DailyForecast>,
    val Headline: Headline
)