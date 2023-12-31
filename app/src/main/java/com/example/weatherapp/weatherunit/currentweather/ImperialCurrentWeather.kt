package com.example.weatherapp.weatherunit.currentweather

import androidx.room.ColumnInfo

class ImperialCurrentWeather(
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
    @ColumnInfo(name = "temperature_Imperial_Value")
    override val Temperature: Double,
    @ColumnInfo(name = "temperature_Imperial_Unit")
    override val Unit: String,
    @ColumnInfo(name = "WeatherIcon")
    override val WeatherIcon: Int,
    @ColumnInfo(name = "WeatherText")
    override val WeatherText: String,
    override val UVIndex: Int,
    @ColumnInfo(name = "pressure_Imperial_Value")
    override val pressure: Double,
    @ColumnInfo(name = "pressure_Imperial_Unit")
    override val pressureUnit: String,
    @ColumnInfo(name = "real_feel_temperature_imperial_Value")
    override val RealFeelTemperature: Double,
    @ColumnInfo(name = "RelativeHumidity")
    override val RelativeHumidity: Int,
    @ColumnInfo(name = "wind_speed_imperial_Value")
    override val windSpeed: Double,
    @ColumnInfo(name = "wind_speed_imperial_Unit")
    override val windSpeedUnit: String,
    @ColumnInfo(name = "wind_direction_Localized")
    override val windDirection: String


):UnitLocalizedCurrentWeather