package com.example.weatherapp.entity.currentweather

import androidx.room.ColumnInfo

class MetricCurrentWeather(
    @ColumnInfo(name = "EpochTime")
    override val EpochTime: Long,
    @ColumnInfo(name = "HasPrecipitation")
    override val HasPrecipitation: Boolean,
    @ColumnInfo(name = "IsDayTime")
    override val IsDayTime: Boolean,
    @ColumnInfo(name = "Link")
    override val Link: String,
    @ColumnInfo(name = "LocalObservationDateTime")
    override val LocalObservationDateTime: String,
    @ColumnInfo(name = "MobileLink")
    override val MobileLink: String,
//    @ColumnInfo(name = "PrecipitationType")
//    override val PrecipitationType: Any,
    @ColumnInfo(name = "temperature_Metric_Value")
    override val Temperature: Double,
    @ColumnInfo(name = "temperature_Metric_Unit")
    override val Unit: String,
    @ColumnInfo(name = "WeatherIcon")
    override val WeatherIcon: Int,
    @ColumnInfo(name = "WeatherText")
    override val WeatherText: String
) :UnitLocalizedCurrentWeather