package com.example.weatherapp.weatherunit.forecast24h

import androidx.room.ColumnInfo

class Metric24hForecast (
    @ColumnInfo(name = "DateTime")
    override val DateTime: String,
    @ColumnInfo(name = "EpochDateTime")
    override val EpochDateTime: Int,
    @ColumnInfo(name = "temperature_Value")
    override val temperature_value: Double,
    @ColumnInfo(name = "temperature_Unit")
    override val temperature_unit: String,
    @ColumnInfo(name = "WeatherIcon")
    override val WeatherIcon: Int,
    @ColumnInfo(name = "IconPhrase")
    override val IconPhrase: String,
    @ColumnInfo(name = "wind_speed_Value")
    override val WindSpeed: Double?,
//    @ColumnInfo(name = "wind_direction_Degrees")
//    override val WindDirection: String,
    @ColumnInfo(name = "wind_speed_Unit")
    override val WindUnit: String?
):UnitLocalized24hForecast