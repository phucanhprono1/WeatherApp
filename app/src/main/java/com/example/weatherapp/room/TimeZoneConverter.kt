package com.example.weatherapp.room

import androidx.room.TypeConverter
import com.example.weatherapp.response.geolocation.TimeZone
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TimeZoneConverter {
    @TypeConverter
    fun fromString(value: String): TimeZone {
        val type = object : TypeToken<TimeZone>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun toString(value: TimeZone): String {
        return Gson().toJson(value)
    }
}