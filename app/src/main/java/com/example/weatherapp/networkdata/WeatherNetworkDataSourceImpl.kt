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
    private val _downloadedCurrentWeather = MutableLiveData<List<CurrentWeatherResponse>>()
    override val downloadedCurrentWeather: LiveData<List<CurrentWeatherResponse>>
        get() = _downloadedCurrentWeather

    override suspend fun fetchCurrentWeather(locationKey: List<String>, language: String) {
        try {
            val fetchlist = mutableListOf<CurrentWeatherResponse>()
            for (i in locationKey.indices) {
                try {
                    val fetchedCurrentWeather = GlobalScope.async {
                        serviceFactory.createWeatherApi()
                            .getCurrentWeather(locationKey[i], serviceFactory.API_KEY, language, true)
                    }.await()
                    if (fetchedCurrentWeather.isSuccessful && fetchedCurrentWeather.body() != null) {
                        fetchlist.add(fetchedCurrentWeather.body()!!)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            _downloadedCurrentWeather.postValue(fetchlist)
        } catch (e: Exception) {
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
                    .get5dayForecast(locationKey,serviceFactory.API_KEY,language,true,true)
            } .await()
        if(fetchedFutureWeather.isSuccessful && fetchedFutureWeather.body()!=null){
            _downloadedFutureWeather.postValue(fetchedFutureWeather.body())
        }

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
            if (fetchedHourlyForecast.isSuccessful && fetchedHourlyForecast.body()!=null)
                _downloadedHourlyForecast.postValue(fetchedHourlyForecast.body())
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}