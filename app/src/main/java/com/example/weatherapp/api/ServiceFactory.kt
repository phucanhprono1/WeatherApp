package com.example.weatherapp.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceFactory {
    private const val BASE_URL = "https://dataservice.accuweather.com/"
    const val API_KEY = "sB1ThIwY2wjoS80a5dwzSr3m7ctTBqaZ"
 //   const val API_KEY = "hGYgGOfw7KbRkutaE2or1vVnKFno1njx"

    private val okHttpClient = OkHttpClient.Builder()
                                .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    fun createLocationApi(): LocationApi {
        return retrofit.create(LocationApi::class.java)
    }
    fun createWeatherApi(): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }
}