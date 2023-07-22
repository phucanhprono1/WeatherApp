package com.example.weatherapp.repository

import androidx.lifecycle.LiveData
import com.example.weatherapp.entity.currentweather.UnitLocalizedCurrentWeather
import com.example.weatherapp.networkdata.WeatherNetworkDataSource
import com.example.weatherapp.response.currentweather.CurrentWeatherResponse
import com.example.weatherapp.room.CurrentWeatherDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime
import java.util.Locale

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource
):ForecastRepository {
    init {
        weatherNetworkDataSource.apply {
            downloadedCurrentWeather.observeForever { newCurrentWeather->
                persistFetchedCurrentWeather(newCurrentWeather)
            }

        }
    }
    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse){
        GlobalScope.launch (Dispatchers.IO){
            currentWeatherDao.upsert(fetchedWeather.get(0))
        }


    }
    override suspend fun getCurrentWeather(metric: Boolean,locationKey:String): LiveData<out UnitLocalizedCurrentWeather> {

        return withContext(Dispatchers.IO){
            initWeatherData(locationKey)
            return@withContext if (metric) currentWeatherDao.getWeatherMetric()
            else currentWeatherDao.getWeatherImperial()
        }
    }
    private suspend fun initWeatherData(locationKey:String){
        if(isFetchCurrentNeeded(ZonedDateTime.now())){
            fetchCurrentWeather(locationKey)
        }
    }
    private suspend fun fetchCurrentWeather(locationKey:String){
        weatherNetworkDataSource.fetchCurrentWeather(locationKey, Locale.getDefault().language)
    }
    private fun isFetchCurrentNeeded(lastFetchedTime: ZonedDateTime): Boolean {
        val twoMinutesAgo = ZonedDateTime.now().minusMinutes(2)
        return lastFetchedTime.isBefore(twoMinutesAgo)
    }
}