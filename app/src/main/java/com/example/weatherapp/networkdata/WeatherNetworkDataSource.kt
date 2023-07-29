package com.example.weatherapp.networkdata

import androidx.lifecycle.LiveData
import com.example.weatherapp.response.FivedaysForecast.DailyForecast
import com.example.weatherapp.response.FivedaysForecast.FiveDaysForecast
import com.example.weatherapp.response.currentweather.CurrentWeatherResponse
import com.example.weatherapp.response.forecast24h.Forecast24h

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
    suspend fun fetchCurrentWeather(locationKey: String,language:String)
    val downloadedFutureWeather: LiveData<FiveDaysForecast>
    suspend fun fetchFutureWeather(locationKey: String,language:String)
    val downloadedHourlyForecast: LiveData<Forecast24h>
    suspend fun fetchHourlyForecast(locationKey: String,language:String)

}