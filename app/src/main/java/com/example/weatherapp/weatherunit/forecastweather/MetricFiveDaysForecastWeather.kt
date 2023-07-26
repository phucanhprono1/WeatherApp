package com.example.weatherapp.weatherunit.forecastweather

import androidx.room.ColumnInfo

class MetricFiveDaysForecastWeather (
    @ColumnInfo(name = "Date")
    override val date: String,
    @ColumnInfo(name = "temperature_maximum_Value")
    override val maxTemperature: Double,
    @ColumnInfo(name = "temperature_minimum_Value")
    override val minTemperature: Double,
    @ColumnInfo(name = "day_Icon")
    override val icon_day: Int,
    @ColumnInfo(name = "day_IconPhrase")
    override val iconPhrase_day: String,
    @ColumnInfo(name = "night_Icon")
    override val icon_night: Int,
    @ColumnInfo(name = "night_IconPhrase")
    override val iconPhrase_night: String,

    ): UnitLocalizedFiveDaysForecastWeather
