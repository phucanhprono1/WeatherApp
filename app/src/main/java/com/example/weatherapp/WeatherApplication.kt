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

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)

    }
}