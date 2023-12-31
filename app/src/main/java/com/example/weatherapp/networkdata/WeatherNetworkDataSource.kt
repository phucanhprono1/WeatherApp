package com.example.weatherapp.networkdata

import androidx.lifecycle.LiveData
import com.example.weatherapp.response.FivedaysForecast.DailyForecast
import com.example.weatherapp.response.FivedaysForecast.FiveDaysForecast
import com.example.weatherapp.response.currentweather.CurrentWeatherResponse
import com.example.weatherapp.response.forecast24h.Forecast24h

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<List<CurrentWeatherResponse>>
    suspend fun fetchCurrentWeather(locationKey: LiveData<List<KeyTimeZone>>,language:String)
    val downloadedFutureWeather: LiveData<List<FiveDaysForecast>>
    suspend fun fetchFutureWeather(locationKey: LiveData<List<KeyTimeZone>>,language:String)
    val downloadedHourlyForecast: LiveData<List<Forecast24h>>
    suspend fun fetchHourlyForecast(locationKey: LiveData<List<KeyTimeZone>>,language:String)

}