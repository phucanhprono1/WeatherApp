package com.example.weatherapp.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherapp.response.FivedaysForecast.DailyForecast
import com.example.weatherapp.response.currentweather.CurrentWeatherResponseItem
import com.example.weatherapp.response.forecast24h.Forecast24hItem
import com.example.weatherapp.response.geolocation.LocationKeyResponse

@Entity(tableName = "location_info")
data class LocationInfo(
    @PrimaryKey
    val Key: String,
    val LocationKeyResponse : LocationKeyResponse,
    val CurrentWeatherResponse : CurrentWeatherResponseItem ?= null,

){

}
