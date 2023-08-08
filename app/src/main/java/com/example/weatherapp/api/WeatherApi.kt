package com.example.weatherapp.api

import com.example.weatherapp.response.FivedaysForecast.FiveDaysForecast
import com.example.weatherapp.response.currentweather.CurrentWeatherResponse
import com.example.weatherapp.response.currentweather.CurrentWeatherResponseItem
import com.example.weatherapp.response.forecast24h.Forecast24h

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApi {
    @GET("currentconditions/v1/{locationKey}")
    suspend fun getCurrentWeather(
        @Path("locationKey") locationKey: String,
        @Query("apikey") apiKey: String,
        @Query("language") language: String,
        @Query("details") details: Boolean=true,

    ): Response<CurrentWeatherResponse>
    @GET("/forecasts/v1/daily/5day/{locationKey}")
    suspend fun get5dayForecast(
        @Path("locationKey") locationKey: String,
        @Query("apikey") apiKey: String,
        @Query("language") language: String,
        @Query("details") details: Boolean,
        @Query("metric") metric: Boolean
        ): Response<FiveDaysForecast>
    @GET("/forecasts/v1/hourly/12hour/{locationKey}")
    suspend fun get24hourForecast(
        @Path("locationKey") locationKey: String,
        @Query("apikey") apiKey: String,
        @Query("language") language: String,
        @Query("details") details: Boolean,
        @Query("metric") metric: Boolean
    ): Response<Forecast24h>
}