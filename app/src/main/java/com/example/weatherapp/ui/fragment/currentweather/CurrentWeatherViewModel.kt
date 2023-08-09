package com.example.weatherapp.ui.fragment.currentweather

import android.content.Context

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.ServiceFactory
import com.example.weatherapp.provider.eagerDeferred
import com.example.weatherapp.ui.fragment.WeatherViewModel
import com.example.weatherapp.provider.lazyDeferred
import com.example.weatherapp.repository.ForecastRepository
import com.example.weatherapp.weatherunit.currentweather.UnitLocalizedCurrentWeather
import dagger.hilt.android.lifecycle.HiltViewModel

import org.threeten.bp.LocalDateTime

import javax.inject.Inject

@HiltViewModel
class CurrentWeatherViewModel  @Inject constructor(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    private val forecastRepository: ForecastRepository
) : WeatherViewModel(forecastRepository,context, lifecycleOwner) {
    var key:String = ""

    fun currentWeatherNonLiveByLocationKey(key:String):UnitLocalizedCurrentWeather {
        return forecastRepository.getWeatherNonLiveByLocationKey(key,true)
    }
    val forecastWeatherNonLive = forecastRepository.get5dayForecastNonLive(LocalDateTime.now(),true)
    val hourlyForecastNonLive = forecastRepository.getHourlyForecastNonLive(LocalDateTime.now(),true)
    val currentWeatherByLocationKey by lazyDeferred {
        forecastRepository.getCurrentWeatherByLocationKey(key,true)
    }
    val forecastWeather by lazyDeferred {
        forecastRepository.getFutureWeatherList(LocalDateTime.now(),true)
    }
    val hourlyForecast by lazyDeferred {
        forecastRepository.getHourlyForecastList(LocalDateTime.now(),true)
    }
}