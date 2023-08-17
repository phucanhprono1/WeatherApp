package com.example.weatherapp.repository

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.networkdata.FORECAST_DAYS_COUNT
import com.example.weatherapp.networkdata.FORECAST_HOURS_COUNT
import com.example.weatherapp.networkdata.KeyTimeZone
import com.example.weatherapp.weatherunit.currentweather.UnitLocalizedCurrentWeather
import com.example.weatherapp.networkdata.WeatherNetworkDataSource
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
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import java.security.Key
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
    private fun persistFetchHourlyForecast(fetchedWeather: List<Forecast24h>) {
        fun deleteOldForecastData(timezone:String, locationKey:String) {
            val currentTime = ZonedDateTime.now(ZoneId.of(timezone))
            hourlyForecastDao.deleteOldEntriesByLocationKey(currentTime, locationKey)
        }

        GlobalScope.launch(Dispatchers.IO) {
//            deleteOldForecastData()

            for (i in fetchedWeather.indices){
                if(!fetchedWeather[i].isNullOrEmpty()){
                    fetchedWeather[i][0].timezone?.let { fetchedWeather[i][0].locationKey?.let { it1 -> deleteOldForecastData(it, it1) } }
                    if (hourlyForecastDao.countEntriesForLocationKey(fetchedWeather[i][0].locationKey!!) == 0){
                        hourlyForecastDao.insert(fetchedWeather[i])
                    }
                }
            }

            Log.d("ForecastRepositoryImpl", "fetchHourlyWeather: $fetchedWeather")
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: List<CurrentWeatherResponse>){
        GlobalScope.launch (Dispatchers.IO){
//            val listKey = getWeatherLocationKey();

            for(i in fetchedWeather.indices){
                if (i==0){
                    sharedPreferences.getString("LOCATION_KEY","")
                        ?.let {fetchedWeather[i][0].id = it}
                    currentWeatherDao.upsert(fetchedWeather[i][0])
                }
                else{
                    locationRepository.getAllCity()[i-1].Key?.let { fetchedWeather[i][0].id = it }
                    currentWeatherDao.upsert(fetchedWeather[i][0])
                }
                Log.d("ForecastRepositoryImpl", "persistFetchedCurrentWeather: ${fetchedWeather[i]}")
            }

        }

    }
    private fun persistFetchFutureWeather(fetchedWeather: List<FiveDaysForecast>) {
        fun deleteOldForecastData(timezone: String, locationKey: String) {
            val today = ZonedDateTime.now(ZoneId.of(timezone))// Convert to LocalDate
            futureWeatherDao.deleteOldEntriesByLocationKey( locationKey)
        }
        GlobalScope.launch(Dispatchers.IO) {
            for (i in fetchedWeather.indices) {
                if (!fetchedWeather[i].DailyForecasts.isNullOrEmpty()) {
                    val timezone = fetchedWeather[i].DailyForecasts[0].timezone
                    val locationKey = fetchedWeather[i].DailyForecasts[0].locationKey
                    deleteOldForecastData(timezone!!, locationKey!!)

                    if (futureWeatherDao.countEntriesForLocationKey(locationKey) < 5) {
                        futureWeatherDao.insert(fetchedWeather[i].DailyForecasts)
                    }
                }
            }
            Log.d("ForecastRepositoryImpl", "persistFetchedFutureWeather: $fetchedWeather")
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
        return if (metric) futureWeatherDao.getFutureWeatherMetricNonLive(startDate)
        else futureWeatherDao.getFutureWeatherMetricNonLive(startDate)
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

    override suspend fun getFutureWeatherListByLocationKey(
        startDate: LocalDate,
        locationKey: String,
        metric: Boolean
    ): LiveData<out List<UnitLocalizedFiveDaysForecastWeather>> {
        return withContext(Dispatchers.IO){
            initWeatherData()
            return@withContext if (metric) futureWeatherDao.getFutureWeatherMetricByLocationKey(locationKey)
            else futureWeatherDao.getFutureWeatherMetricByLocationKey(locationKey)
        }
    }

    override fun get5dayForecastNonLiveByLocationKey(
        startDate: LocalDateTime,
        locationKey: String,
        metric: Boolean
    ): List<UnitLocalizedFiveDaysForecastWeather> {
        return if (metric) futureWeatherDao.getFutureWeatherMetricByLocationKeyNonLive(locationKey)
        else futureWeatherDao.getFutureWeatherMetricByLocationKeyNonLive(locationKey)
    }

    override suspend fun getHourlyForecastListByLocationKey(
        startDate: LocalDateTime,
        locationKey: String,
        metric: Boolean
    ): LiveData<out List<UnitLocalized24hForecast>> {
        return withContext(Dispatchers.IO){
            initWeatherData()
            return@withContext if (metric) hourlyForecastDao.getHourlyForecastMetricByLocationKey(locationKey)
            else hourlyForecastDao.getHourlyForecastMetricByLocationKey(locationKey)
        }
    }

    override fun getHourlyForecastNonLiveByLocationKey(
        startDate: LocalDateTime,
        locationKey: String,
        metric: Boolean
    ): List<UnitLocalized24hForecast> {
        return if (metric) hourlyForecastDao.getHourlyForecastMetricByLocationKeyNonLive(locationKey)
        else hourlyForecastDao.getHourlyForecastMetricByLocationKeyNonLive(locationKey)
    }


    private suspend fun initWeatherData(){
        //val lastFetchedTime = currentWeatherDao.getWeatherMetricNonLive().zonedDateTime
        getListKey()
//        fetchCurrentWeather()
//        fetchFutureWeather()
//        fetchHourlyForecast()
//        if (isFetchFutureNeeded()){
//            fetchFutureWeather()
//        }
//        if (isFetchHourlyNeeded()){
//            fetchHourlyForecast()
//        }

    }

    private val _listKey = MutableLiveData<List<KeyTimeZone>>()
    private val listKey: LiveData<List<KeyTimeZone>>
        get() = _listKey

    suspend fun getListKey(){
        val newListKey = ArrayList<KeyTimeZone>()
        sharedPreferences.getString("LOCATION_KEY", "")?.let {it1 ->
            sharedPreferences.getString("TIME_ZONE", "")?.let {
                newListKey.add(KeyTimeZone(it1, it))
            }
            for (location in locationRepository.getAllCity()) {
                newListKey.add(KeyTimeZone(location.Key, location.TimeZone.Name))
            }
        }

        // Update the _listKey LiveData with the new value
        _listKey.postValue(newListKey)
        fetchCurrentWeather()
        fetchFutureWeather()
        fetchHourlyForecast()
    }
    private suspend fun fetchCurrentWeather() {

        weatherNetworkDataSource.fetchCurrentWeather(listKey, Locale.getDefault().language)
    }
    private suspend fun fetchFutureWeather(){
        weatherNetworkDataSource.fetchFutureWeather(listKey, Locale.getDefault().language)
    }
    private suspend fun fetchHourlyForecast(){
        weatherNetworkDataSource.fetchHourlyForecast(listKey, Locale.getDefault().language)
    }

    private fun isFetchCurrentNeeded(lastFetchedTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchedTime.isBefore(thirtyMinutesAgo)
    }
    private fun isFetchHourlyNeeded(): Boolean {
        val today = LocalDateTime.now()
        val hourlyForecastCount = hourlyForecastDao.countHourlyForecast(today)
        val totalLocationKeys = _listKey.value?.size ?: 0
        return hourlyForecastCount < FORECAST_HOURS_COUNT * totalLocationKeys
    }

    private fun isFetchFutureNeeded(): Boolean {
        val today = LocalDateTime.now()
        val futureWeatherCount = futureWeatherDao.countFutureWeather(today)
        val totalLocationKeys = _listKey.value?.size ?: 0
        return futureWeatherCount < FORECAST_DAYS_COUNT * totalLocationKeys
    }
}