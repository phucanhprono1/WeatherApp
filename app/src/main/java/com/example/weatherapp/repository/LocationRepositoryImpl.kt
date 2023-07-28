package com.example.weatherapp.repository

import android.util.Log
import com.example.weatherapp.api.ServiceFactory
import com.example.weatherapp.response.geolocation.LocationKeyResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(

        )  : LocationRepository {

    override suspend fun searchCities(cityName: String): List<LocationKeyResponse>? {
        val response = ServiceFactory.createLocationApi().getLocationByName(ServiceFactory.API_KEY, cityName)
        Log.d("SEARCHING", "SUCCESS")
        if(response.isSuccessful && response.body() != null){
            Log.d("SUCCESS", "SUCCESS")
            Log.d("SUCCESS", response.body().toString())
            return response.body()!!
        }

        return response.body()
    }

}
