package com.example.weatherapp.repository

import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.response.geolocation.LocationKeyResponse

interface LocationRepository {

    suspend fun searchCities(cityName : String) : List<LocationKeyResponse>?

    fun getAllCity() : List<LocationKeyResponse>
    fun insertNewCity(locationKeyResponse: LocationKeyResponse)

    fun deleteCity(locationKeyResponse: LocationKeyResponse)
}