package com.example.weatherapp

import android.app.Application
import android.content.SharedPreferences
import com.example.weatherapp.api.ServiceFactory
import com.example.weatherapp.networkdata.WeatherNetworkDataSource
import com.example.weatherapp.networkdata.WeatherNetworkDataSourceImpl
import com.example.weatherapp.room.ForecastDatabase
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltAndroidApp
class WeatherApplication: Application() {
//    @Inject
//    lateinit var forecastDatabase: ForecastDatabase
//    @Inject
//    lateinit var serviceFactory: ServiceFactory
//    @Inject
//    lateinit var weatherNetworkDataSource: WeatherNetworkDataSource
//    @Inject
//    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate() {
        super.onCreate()
//        forecastDatabase = ForecastDatabase.invoke(this)
        AndroidThreeTen.init(this)
//        weatherNetworkDataSource = WeatherNetworkDataSourceImpl(serviceFactory)
//        GlobalScope.launch(Dispatchers.IO) {
//            sharedPreferences.getString("LOCATION_KEY", "")?.let {
//                (weatherNetworkDataSource as WeatherNetworkDataSourceImpl).fetchCurrentWeather(
//                    it,
//                    Locale.getDefault().language
//                )
//            }
//        }
    }
}