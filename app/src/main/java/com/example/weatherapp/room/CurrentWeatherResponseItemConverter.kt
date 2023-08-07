package com.example.weatherapp.room

import androidx.room.TypeConverter
import com.example.weatherapp.response.currentweather.CurrentWeatherResponseItem
import com.google.gson.Gson

object CurrentWeatherResponseItemConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromCurrentWeatherResponseItem(currentWeatherResponseItem: CurrentWeatherResponseItem): String {
        return gson.toJson(currentWeatherResponseItem)
    }

    @TypeConverter
    fun toCurrentWeatherResponseItem(json: String): CurrentWeatherResponseItem {
        return gson.fromJson(json, CurrentWeatherResponseItem::class.java)
    }
}