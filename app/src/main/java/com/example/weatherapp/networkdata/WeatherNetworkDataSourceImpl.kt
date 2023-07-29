package com.example.weatherapp.networkdata

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.api.ServiceFactory
import com.example.weatherapp.response.FivedaysForecast.DailyForecast
import com.example.weatherapp.response.FivedaysForecast.FiveDaysForecast
import com.example.weatherapp.response.currentweather.CurrentWeatherResponse
import com.example.weatherapp.response.forecast24h.Forecast24h
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import javax.inject.Inject
import javax.inject.Singleton
const val FORECAST_DAYS_COUNT = 5
const val FORECAST_HOURS_COUNT = 12
@Singleton
class WeatherNetworkDataSourceImpl @Inject constructor(
    private val serviceFactory:ServiceFactory,

): WeatherNetworkDataSource {
    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    override suspend fun fetchCurrentWeather(locationKey: String, language: String) {
        try {
            val fetchedCurrentWeather = serviceFactory.createWeatherApi()
                .getCurrentWeather(locationKey,serviceFactory.API_KEY,language,true)
                .await()
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
                    .get5dayForecast(locationKey,serviceFactory.API_KEY,language,true)
            } .await()

            _downloadedFutureWeather.postValue(fetchedFutureWeather)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    val _downloadedHourlyForecast = MutableLiveData<Forecast24h>()
    override val downloadedHourlyForecast: LiveData<Forecast24h>
        get() = _downloadedHourlyForecast


    override suspend fun fetchHourlyForecast(locationKey: String, language: String) {
        try {
            val fetchedHourlyForecast = GlobalScope.async {
                serviceFactory.createWeatherApi()
                    .get24hourForecast(locationKey,serviceFactory.API_KEY,language,true,true)
            } .await()

            _downloadedHourlyForecast.postValue(fetchedHourlyForecast)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}