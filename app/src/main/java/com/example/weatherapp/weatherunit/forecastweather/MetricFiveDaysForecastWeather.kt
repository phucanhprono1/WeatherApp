package com.example.weatherapp.weatherunit.forecastweather

import androidx.room.ColumnInfo

class MetricFiveDaysForecastWeather (
    @ColumnInfo(name = "Date")
    override val date: String,
    @ColumnInfo(name = "timezone")
    override val timezone: String,
    @ColumnInfo(name = "MobileLink")
    override val link: String,
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
    @ColumnInfo(name = "day_wind_speed_Value")
    override val WindSpeed: Double?,
 //   @ColumnInfo(name = "wind_direction_Degrees")
//    override val WindDirection: String,
    @ColumnInfo(name = "day_wind_speed_Unit")
    override val WindUnit: String?,
): UnitLocalizedFiveDaysForecastWeather
