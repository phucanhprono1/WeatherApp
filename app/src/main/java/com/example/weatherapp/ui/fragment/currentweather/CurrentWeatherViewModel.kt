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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.invoke
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate

import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherViewModel  @Inject constructor(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    private val forecastRepository: ForecastRepository
) : WeatherViewModel(forecastRepository,context, lifecycleOwner) {

    val currentWeatherNonLive = forecastRepository.getWeatherNonLive(true)
    val forecastWeatherNonLive = forecastRepository.get5dayForecastNonLive(LocalDate.now(),true)
    val currentWeather by eagerDeferred {
        forecastRepository.getCurrentWeather(true)
    }
    val forecastWeather by lazyDeferred {
        forecastRepository.getFutureWeatherList(LocalDate.now(),true)
    }

    val temperatureData = arrayOf(89f, 91f, 93f, 95f, 96f, 97f, 99f, 98f, 96f, 93f, 91f, 88f)

}