package com.example.weatherapp.repository

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.weatherapp.networkdata.FORECAST_DAYS_COUNT
import com.example.weatherapp.networkdata.FORECAST_HOURS_COUNT
import com.example.weatherapp.weatherunit.currentweather.UnitLocalizedCurrentWeather
import com.example.weatherapp.networkdata.WeatherNetworkDataSource
import com.example.weatherapp.response.FivedaysForecast.DailyForecast
import com.example.weatherapp.response.FivedaysForecast.FiveDaysForecast
import com.example.weatherapp.response.currentweather.CurrentWeatherResponse
import com.example.weatherapp.response.forecast24h.Forecast24h
import com.example.weatherapp.room.CurrentWeatherDao
import com.example.weatherapp.room.FutureWeatherDao
import com.example.weatherapp.room.HourlyForecastDao
import com.example.weatherapp.weatherunit.forecast24h.UnitLocalized24hForecast
import com.example.weatherapp.weatherunit.forecastweather.UnitLocalizedFiveDaysForecastWeather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZonedDateTime
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForecastRepositoryImpl @Inject constructor(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val sharedPreferences: SharedPreferences,
    private val futureWeatherDao: FutureWeatherDao,
    private val hourlyForecastDao: HourlyForecastDao,
    private val locationRepository: LocationRepository
):ForecastRepository {

    init {
        weatherNetworkDataSource.apply {
            downloadedCurrentWeather.observeForever { newCurrentWeather->
                persistFetchedCurrentWeather(newCurrentWeather)
            }
            downloadedFutureWeather.observeForever { newFutureWeather->
                persistFetchFutureWeather(newFutureWeather)
            }
            downloadedHourlyForecast.observeForever { newHourlyForecast->
                persistFetchHourlyForecast(newHourlyForecast)
            }
        }
    }
    private fun persistFetchHourlyForecast(fetchedWeather: Forecast24h) {
        fun deleteOldForecastData(){
            val thistime = LocalDateTime.now()
            hourlyForecastDao.deleteOldEntries(thistime)
        }
        GlobalScope.launch(Dispatchers.IO) {
            deleteOldForecastData()
            hourlyForecastDao.insert(fetchedWeather)
            Log.d("ForecastRepositoryImpl", "persistFetchedHourlyForecast: ${fetchedWeather}")
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: List<CurrentWeatherResponse>){
        GlobalScope.launch (Dispatchers.IO){
//            val listKey = getWeatherLocationKey();

            for(i in 0..fetchedWeather.size-1){
                if (i==0){
                    sharedPreferences.getString("LOCATION_KEY","")
                        ?.let {fetchedWeather[i][0].id = it}
                    currentWeatherDao.upsert(fetchedWeather[i][0])
                }
                else{
                    locationRepository.getAllCity().get(i-1).Key?.let { fetchedWeather[i][0].id = it }
                    currentWeatherDao.upsert(fetchedWeather[i][0])
                }
                Log.d("ForecastRepositoryImpl", "persistFetchedCurrentWeather: ${fetchedWeather[i]}")
            }

        }

    }
    private fun persistFetchFutureWeather(fetchedWeather: FiveDaysForecast) {
        fun deleteOldForecastData(){
            val today = LocalDateTime.now()
            futureWeatherDao.deleteOldEntries(today)
        }
        GlobalScope.launch(Dispatchers.IO) {
            deleteOldForecastData()
            futureWeatherDao.insert(fetchedWeather.DailyForecasts)
            Log.d("ForecastRepositoryImpl", "persistFetchedFutureWeather: ${fetchedWeather}")
        }
    }
    override suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitLocalizedCurrentWeather> {

        return withContext(Dispatchers.IO){
            Log.d("ForecastRepositoryImpl", "getCurrentWeather: ${currentWeatherDao.getWeatherMetric()}")
            initWeatherData()
            return@withContext if (metric) currentWeatherDao.getWeatherMetric()
            else currentWeatherDao.getWeatherImperial()
        }
    }
    override suspend fun getCurrentWeatherByLocationKey(locationKey: String, metric: Boolean): LiveData<out UnitLocalizedCurrentWeather> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            Log.d("ForecastRepositoryImpl", "getCurrentWeatherByLocationKey: ${currentWeatherDao.getWeatherMetricByLocationKey(locationKey)}")
            return@withContext if (metric) currentWeatherDao.getWeatherMetricByLocationKey(locationKey)
            else currentWeatherDao.getWeatherImperialByLocationKey(locationKey)
        }
    }
    override fun getWeatherNonLive(metric: Boolean): UnitLocalizedCurrentWeather {
        return if (metric) currentWeatherDao.getWeatherMetricNonLive()
        else currentWeatherDao.getWeatherImperialNonLive()
    }

    override fun getWeatherNonLiveByLocationKey(
        locationKey: String,
        metric: Boolean
    ): UnitLocalizedCurrentWeather {
        return if (metric) currentWeatherDao.getWeatherMetricByLocationKeyNonLive(locationKey)
        else currentWeatherDao.getWeatherImperialByLocationKeyNonLive(locationKey)
    }

    override suspend fun getFutureWeatherList(
        startDate: LocalDateTime,
        metric: Boolean
    ): LiveData<out List< UnitLocalizedFiveDaysForecastWeather>> {
        return withContext(Dispatchers.IO){
            initWeatherData()
            return@withContext if (metric) futureWeatherDao.getFutureWeatherMetric(startDate)
            else futureWeatherDao.getFutureWeatherMetric(startDate)
        }
    }

    override fun get5dayForecastNonLive(startDate: LocalDateTime, metric: Boolean): List<UnitLocalizedFiveDaysForecastWeather> {
        if (metric) return futureWeatherDao.getFutureWeatherMetricNonLive(startDate)
        else return futureWeatherDao.getFutureWeatherMetricNonLive(startDate)
    }

    override suspend fun getHourlyForecastList(
        startDate: LocalDateTime,
        metric: Boolean
    ): LiveData<out List<UnitLocalized24hForecast>> {
        return withContext(Dispatchers.IO){
            initWeatherData()
            return@withContext if (metric) hourlyForecastDao.getHourlyForecastMetric(startDate)
            else hourlyForecastDao.getHourlyForecastMetric(startDate)
        }
    }

    override fun getHourlyForecastNonLive(
        startDate: LocalDateTime,
        metric: Boolean
    ): List<UnitLocalized24hForecast> {
        return if (metric) hourlyForecastDao.getHourlyForecastMetricNonLive(startDate)
        else hourlyForecastDao.getHourlyForecastMetricNonLive(startDate)
    }


    private suspend fun initWeatherData(){
        //val lastFetchedTime = currentWeatherDao.getWeatherMetricNonLive().zonedDateTime
        fetchCurrentWeather()
        fetchFutureWeather()
        fetchHourlyForecast()
        if (isFetchFutureNeeded()){
            fetchFutureWeather()
        }
        if (isFetchHourlyNeeded()){
            fetchHourlyForecast()
        }

    }
//    private suspend fun getWeatherLocationKey():List<String>{
//        var listKey = ArrayList<String>()
//        var locationKey = ""
//        sharedPreferences.getString("LOCATION_KEY","")?.let {
//            locationKey = it
//        }
//        listKey.add(locationKey)
//        for (location in locationRepository.getAllCity()){
//            listKey.add(location.Key)
//        }
//        return listKey
//    }
    private suspend fun fetchCurrentWeather(){
        var listKey = ArrayList<String>()
        sharedPreferences.getString("LOCATION_KEY","")?.let {
            listKey.add(it)
            for (location in locationRepository.getAllCity()){
                listKey.add(location.Key)
            }
        }

        weatherNetworkDataSource.fetchCurrentWeather(listKey, Locale.getDefault().language)
    }
    private suspend fun fetchFutureWeather(){
        sharedPreferences.getString("LOCATION_KEY","")
            ?.let { weatherNetworkDataSource.fetchFutureWeather(it, Locale.getDefault().language) }
    }
    private suspend fun fetchHourlyForecast(){
        sharedPreferences.getString("LOCATION_KEY","")
            ?.let { weatherNetworkDataSource.fetchHourlyForecast(it, Locale.getDefault().language) }
    }
    private fun isFetchHourlyNeeded(): Boolean{
        val today = LocalDateTime.now()
        val hourlyForecastCount = hourlyForecastDao.countHourlyForecast(today)
        return hourlyForecastCount < FORECAST_HOURS_COUNT

    }
    private fun isFetchCurrentNeeded(lastFetchedTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchedTime.isBefore(thirtyMinutesAgo)
    }
    private fun isFetchFutureNeeded(): Boolean{
        val today = LocalDateTime.now()
        val futureWeatherCount = futureWeatherDao.countFutureWeather(today)
        return futureWeatherCount < FORECAST_DAYS_COUNT

    }
}