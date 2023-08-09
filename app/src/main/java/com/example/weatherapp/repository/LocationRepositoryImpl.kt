package com.example.weatherapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.weatherapp.api.ServiceFactory
import com.example.weatherapp.response.geolocation.LocationKeyResponse
import com.example.weatherapp.room.LocationDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(
       private val locationDao: LocationDao
        )  : LocationRepository {

    override suspend fun searchCities(cityName: String): List<LocationKeyResponse>? {
        val response = ServiceFactory.createLocationApi().getLocationByName(ServiceFactory.API_KEY, cityName)
        if(response.isSuccessful && response.body() != null){
            return response.body()!!
        }

        return response.body()
    }

    override fun insertNewCity(locationKeyResponse: LocationKeyResponse) {
        locationDao.insertLocation(locationKeyResponse)
    }

    override fun deleteCity(locationKeyResponse: LocationKeyResponse) {
        locationDao.deleteLocation(locationKeyResponse)
        Log.d("Xoa o repo", "HERE")
    }

    override fun getAllCity() : List<LocationKeyResponse>{
        return locationDao.getAllLocation()
    }

    override fun getAllCityLive(): LiveData<List<LocationKeyResponse>> {
        return locationDao.getAllLocationLive()
    }


}
