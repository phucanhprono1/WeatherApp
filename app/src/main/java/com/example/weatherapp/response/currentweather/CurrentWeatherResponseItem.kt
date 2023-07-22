package com.example.weatherapp.response.currentweather

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherapp.config.StaticConfig
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

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
    val WeatherText: String
){
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_WEATHER_ID
    val zonedDateTime: ZonedDateTime
        get() {
            val instant = Instant.ofEpochSecond(EpochTime)

            return ZonedDateTime.ofInstant(instant, ZoneId.of(StaticConfig.tz_id))
        }
}