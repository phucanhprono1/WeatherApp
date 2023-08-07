package com.example.weatherapp.room

import androidx.room.TypeConverter
import com.example.weatherapp.response.FivedaysForecast.DailyForecast
import com.google.gson.Gson

object DailyForecastConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromDailyForecast(dailyForecast: DailyForecast): String {
        return gson.toJson(dailyForecast)
    }

    @TypeConverter
    fun toDailyForecast(json: String): DailyForecast {
        return gson.fromJson(json, DailyForecast::class.java)
    }
}