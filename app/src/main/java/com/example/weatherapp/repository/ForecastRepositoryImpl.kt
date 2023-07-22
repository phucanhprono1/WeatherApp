package com.example.weatherapp.repository

import android.content.SharedPreferences
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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForecastRepositoryImpl @Inject constructor(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val sharedPreferences: SharedPreferences
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
            currentWeatherDao.upsert(fetchedWeather[0])
        }


    }
    override suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitLocalizedCurrentWeather> {

        return withContext(Dispatchers.IO){
            initWeatherData()
            return@withContext if (metric) currentWeatherDao.getWeatherMetric()
            else currentWeatherDao.getWeatherImperial()
        }
    }
    private suspend fun initWeatherData(){
        if(isFetchCurrentNeeded(ZonedDateTime.now())){
            fetchCurrentWeather()
        }
    }
    private suspend fun fetchCurrentWeather(){
        sharedPreferences.getString("LOCATION_KEY","")
            ?.let { weatherNetworkDataSource.fetchCurrentWeather(it, Locale.getDefault().language) }
    }
    private fun isFetchCurrentNeeded(lastFetchedTime: ZonedDateTime): Boolean {
        val twoMinutesAgo = ZonedDateTime.now().minusMinutes(2)
        return lastFetchedTime.isBefore(twoMinutesAgo)
    }
}