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
    private var newDataCount = 0
    private val _downloadedCurrentWeather = MutableLiveData<List<CurrentWeatherResponse>>()
    override val downloadedCurrentWeather: LiveData<List<CurrentWeatherResponse>>
        get() = _downloadedCurrentWeather

    override suspend fun fetchCurrentWeather(locationKey: LiveData<List<KeyTimeZone>>, language: String) {
        val currentLocationKey = locationKey.value
        currentLocationKey?.let {
            try {
                val fetchlist = mutableListOf<CurrentWeatherResponse>()
                for (i in it.indices) {
                    try {
                        val fetchedCurrentWeather = GlobalScope.async {
                            serviceFactory.createWeatherApi()
                                .getCurrentWeather(it[i].Key, serviceFactory.API_KEY, language, true)
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
    }

    private val _downloadedFutureWeather = MutableLiveData<List<FiveDaysForecast>>()
    override val downloadedFutureWeather: LiveData<List<FiveDaysForecast>>
        get() = _downloadedFutureWeather

    override suspend fun fetchFutureWeather(locationKey: LiveData<List<KeyTimeZone>>, language: String) {
        val currentLocationKey = locationKey.value
        currentLocationKey?.let {
            try {
                val fetchList = mutableListOf<FiveDaysForecast>()
                for (key in it) {
                    try {
                        val fetchedFiveDayForecast = GlobalScope.async {
                            serviceFactory.createWeatherApi()
                                .get5dayForecast(key.Key, serviceFactory.API_KEY, language, true, true)
                        }.await()

                        if (fetchedFiveDayForecast.isSuccessful && fetchedFiveDayForecast.body() != null) {
                            val fiveDaysForecast = fetchedFiveDayForecast.body()!!
                            fiveDaysForecast.DailyForecasts.forEach { forecast ->
                                forecast.locationKey = key.Key // Add locationKey
                                forecast.timezone = key.TimeZone// Add timezone
                            }
                            fetchList.add(fiveDaysForecast)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                _downloadedFutureWeather.postValue(fetchList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private val _downloadedHourlyForecast = MutableLiveData<List<Forecast24h>>()
    override val downloadedHourlyForecast: LiveData<List<Forecast24h>>
        get() = _downloadedHourlyForecast

    override suspend fun fetchHourlyForecast(locationKey: LiveData<List<KeyTimeZone>>, language: String) {
        val currentLocationKeys = locationKey.value
        currentLocationKeys?.let {
            try {
                val fetchList = mutableListOf<Forecast24h>()
                for (key in it) {
                    try {
                        val fetchedHourlyForecast = GlobalScope.async {
                            serviceFactory.createWeatherApi()
                                .get24hourForecast(key.Key, serviceFactory.API_KEY, language, true, true)
                        }.await()

                        if (fetchedHourlyForecast.isSuccessful && fetchedHourlyForecast.body() != null) {
                            val forecast24h = fetchedHourlyForecast.body()!!
                            forecast24h.forEach { forecast ->
                                forecast.locationKey = key.Key // Add locationKey
                                forecast.timezone = key.TimeZone // Add timezone
                            }
                            fetchList.add(forecast24h)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                _downloadedHourlyForecast.postValue(fetchList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}