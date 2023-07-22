package com.example.weatherapp.networkdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.api.ServiceFactory
import com.example.weatherapp.response.FivedaysForecast.DailyForecast
import com.example.weatherapp.response.FivedaysForecast.FiveDaysForecast
import com.example.weatherapp.response.currentweather.CurrentWeatherResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class WeatherNetworkDataSourceImpl(
    private val serviceFactory:ServiceFactory
): WeatherNetworkDataSource {
    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    override suspend fun fetchCurrentWeather(locationKey: String, language: String) {
        try {
            val fetchedCurrentWeather = GlobalScope.async {
                serviceFactory.createWeatherApi()
                    .getCurrentWeather(locationKey,serviceFactory.API_KEY,language)
            }.await()
            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    private val _downloadedFutureWeather = MutableLiveData<FiveDaysForecast>()
    override val downloadedFutureWeather: LiveData<FiveDaysForecast>
        get() = _downloadedFutureWeather

    override suspend fun fetchFutureWeather(locationKey: String, language: String) {
        try {
            val fetchedFutureWeather = GlobalScope.async {
                serviceFactory.createWeatherApi()
                    .get5dayForecast(locationKey,serviceFactory.API_KEY,language)
            } .await()

            _downloadedFutureWeather.postValue(fetchedFutureWeather)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}