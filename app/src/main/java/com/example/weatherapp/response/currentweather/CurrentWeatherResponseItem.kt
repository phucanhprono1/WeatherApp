package com.example.weatherapp.response.currentweather

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.annotation.Nonnull


const val CURRENT_WEATHER_ID = 0
@Entity(tableName = "current_weather")
data class CurrentWeatherResponseItem(
    val EpochTime: Long,
    val HasPrecipitation: Boolean,
    val IsDayTime: Boolean,
    val Link: String,
    val LocalObservationDateTime: String,
    val MobileLink: String,
//    val PrecipitationType: Any,
    @Embedded(prefix = "temperature_")
    val Temperature: Temperature,
    val WeatherIcon: Int,
    val WeatherText: String,
    @Embedded(prefix = "pressure_")
    val Pressure: Pressure,
    @Embedded(prefix = "real_feel_temperature_")
    val RealFeelTemperature: RealFeelTemperature,
    val RelativeHumidity: Int,
    val UVIndex: Int,
    val UVIndexText: String,
    @Embedded(prefix = "wind_")
    val Wind: Wind
    ){
    @PrimaryKey(autoGenerate = false)
    @Nonnull
    var id: String=""
}
