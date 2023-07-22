package com.example.weatherapp.provider

import com.example.weatherapp.response.geolocation.LocationKeyResponse

interface LocationProvider {
    suspend fun hasLocationChanged(lastWeatherLocation: LocationKeyResponse): Boolean
    suspend fun getPreferredLocationString(): String
}