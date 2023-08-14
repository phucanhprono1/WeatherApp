package com.example.weatherapp.ui.fragment.currentweather

import android.content.Context

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.ServiceFactory
import com.example.weatherapp.provider.UnitProvider
import com.example.weatherapp.provider.eagerDeferred
import com.example.weatherapp.ui.fragment.WeatherViewModel
import com.example.weatherapp.provider.lazyDeferred
import com.example.weatherapp.repository.ForecastRepository
import com.example.weatherapp.weatherunit.currentweather.UnitLocalizedCurrentWeather
import com.example.weatherapp.weatherunit.forecast24h.UnitLocalized24hForecast
import com.example.weatherapp.weatherunit.forecastweather.UnitLocalizedFiveDaysForecastWeather
import dagger.hilt.android.lifecycle.HiltViewModel

import org.threeten.bp.LocalDateTime

import javax.inject.Inject

@HiltViewModel
class CurrentWeatherViewModel  @Inject constructor(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    unitProvider: UnitProvider,
    private val forecastRepository: ForecastRepository
) : WeatherViewModel(forecastRepository,context,unitProvider, lifecycleOwner) {
    var key:String = ""

    fun currentWeatherNonLiveByLocationKey(key:String,metric:Boolean):UnitLocalizedCurrentWeather {
        return forecastRepository.getWeatherNonLiveByLocationKey(key,metric)
    }
    fun forecastWeatherNonLiveByLocationKey(key:String):List<UnitLocalizedFiveDaysForecastWeather> {
        return forecastRepository.get5dayForecastNonLiveByLocationKey(LocalDateTime.now(),key,super.isMetricUnit)
    }
    fun hourlyForecastNonLiveByLocationKey(key:String):List<UnitLocalized24hForecast> {
        return forecastRepository.getHourlyForecastNonLiveByLocationKey(LocalDateTime.now(),key,super.isMetricUnit)
    }
    val forecastWeatherNonLive = forecastRepository.get5dayForecastNonLive(LocalDateTime.now(),true)
    val hourlyForecastNonLive = forecastRepository.getHourlyForecastNonLive(LocalDateTime.now(),true)
    val currentWeatherByLocationKey by lazyDeferred {
        forecastRepository.getCurrentWeatherByLocationKey(key,super.isMetricUnit)
    }
    val forecastWeatherByLocationKey by lazyDeferred {
        forecastRepository.getFutureWeatherListByLocationKey(LocalDateTime.now(),key,super.isMetricUnit)
    }
    val hourlyForecastByLocationKey by lazyDeferred {
        forecastRepository.getHourlyForecastListByLocationKey(LocalDateTime.now(),key,super.isMetricUnit)
    }
    val forecastWeather by lazyDeferred {
        forecastRepository.getFutureWeatherList(LocalDateTime.now(),true)
    }
    val hourlyForecast by lazyDeferred {
        forecastRepository.getHourlyForecastList(LocalDateTime.now(),true)
    }
}