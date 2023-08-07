package com.example.weatherapp.room


import androidx.room.TypeConverter
import com.example.weatherapp.response.forecast24h.Forecast24hItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Forecast24hItemConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromForecast24hItemArray(forecast24hItems: Array<Forecast24hItem>): String {
        return gson.toJson(forecast24hItems)
    }

    @TypeConverter
    fun toForecast24hItemArray(json: String): Array<Forecast24hItem> {
        val type = object : TypeToken<Array<Forecast24hItem>>() {}.type
        return gson.fromJson(json, type)
    }
}