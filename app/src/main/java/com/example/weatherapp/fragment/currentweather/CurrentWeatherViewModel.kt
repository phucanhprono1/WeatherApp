package com.example.weatherapp.fragment.currentweather

import android.content.Context

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.ServiceFactory
import com.example.weatherapp.fragment.WeatherViewModel
import com.example.weatherapp.provider.lazyDeferred
import com.example.weatherapp.repository.ForecastRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherViewModel  @Inject constructor(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    private val forecastRepository: ForecastRepository
) : WeatherViewModel(forecastRepository,context, lifecycleOwner) {

    val currentWeather by lazyDeferred {
        forecastRepository.getCurrentWeather(true)
    }

    val forecastWeather by lazyDeferred {
        sharedPreferences.getString("LOCATION_KEY", "")?.let {
            try {
                viewModelScope.async(Dispatchers.IO) {
                    ServiceFactory.createWeatherApi().get5dayForecast(it, ServiceFactory.API_KEY,"en-us",true)
                }.await()
            } catch (e: IOException) {
                // Handle IO exception (e.g., network issues)
                // Handle error: e.message
                null
            } catch (e: HttpException) {
                // Handle HTTP exception (e.g., HTTP 503)
                // Handle error: e.message
                null
            } catch (e: Exception) {
                // Handle other exceptions
                // Handle error: e.message
                null
            }
        }
    }
}