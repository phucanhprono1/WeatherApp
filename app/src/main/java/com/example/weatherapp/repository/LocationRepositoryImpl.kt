package com.example.weatherapp.repository

import android.util.Log
import com.example.weatherapp.api.ServiceFactory
import com.example.weatherapp.response.LocationInfo
import com.example.weatherapp.response.geolocation.LocationKeyResponse
import com.example.weatherapp.room.LocationDao
import com.example.weatherapp.room.LocationInfoDao
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(
       private val locationDao: LocationDao,
       private val infoDao : LocationInfoDao
        )  : LocationRepository {

    override suspend fun searchCities(cityName: String): List<LocationKeyResponse>? {
        val response = ServiceFactory.createLocationApi().getLocationByName(ServiceFactory.API_KEY, cityName)
        if(response.isSuccessful && response.body() != null){
            return response.body()!!
        }

        return response.body()
    }

    override fun getAllCity(): List<LocationInfo> {
        return infoDao.getAllLocation()
    }

    override suspend fun insertNewCity(locationKeyResponse: LocationKeyResponse) {


        val fetchedCurrentWeather = ServiceFactory.createWeatherApi()
            .getCurrentWeather(locationKeyResponse.Key,ServiceFactory.API_KEY,"en-us",true)
            .await()

        Log.d("current", fetchedCurrentWeather[0].toString())
        var info = LocationInfo(locationKeyResponse.Key, locationKeyResponse, fetchedCurrentWeather[0])
        infoDao.insertLocation(info)
    }

    override fun deleteCity(locationKeyResponse: LocationInfo) {
        infoDao.deleteLocation(locationKeyResponse)
    }


}
