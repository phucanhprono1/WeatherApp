package com.example.weatherapp.fragment

import android.content.Context
import android.preference.PreferenceManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.weatherapp.api.ServiceFactory
import com.example.weatherapp.provider.lazyDeferred
import com.example.weatherapp.repository.ForecastRepository

abstract class WeatherViewModel(
    private val forecastRepository: ForecastRepository,
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner
) : ViewModel() {

    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

}