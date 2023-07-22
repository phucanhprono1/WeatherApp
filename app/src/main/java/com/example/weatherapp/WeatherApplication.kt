package com.example.weatherapp

import android.app.Application
import com.example.weatherapp.room.ForecastDatabase
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherApplication: Application() {
    val forecastDatabase: ForecastDatabase by lazy {
        ForecastDatabase.invoke(this)
    }
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

    }
}