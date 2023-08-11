package com.example.weatherapp.ui.fragment

import android.content.Context
import android.preference.PreferenceManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.weatherapp.api.ServiceFactory
import com.example.weatherapp.provider.UnitProvider
import com.example.weatherapp.provider.UnitSystem
import com.example.weatherapp.provider.lazyDeferred
import com.example.weatherapp.repository.ForecastRepository
import javax.inject.Inject

abstract class WeatherViewModel(
    private val forecastRepository: ForecastRepository,
    private val context: Context,
    unitProvider: UnitProvider,
    private val lifecycleOwner: LifecycleOwner
) : ViewModel() {

    private val unitSystem = unitProvider.getUnitSystem()

    val isMetricUnit: Boolean
        get() = unitSystem == UnitSystem.METRIC
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

}