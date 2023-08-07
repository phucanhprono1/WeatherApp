package com.example.weatherapp.room

import androidx.room.TypeConverter
import com.example.weatherapp.response.geolocation.LocationKeyResponse
import com.google.gson.Gson

object LocationKeyResponseConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromLocationKeyResponse(locationKeyResponse: LocationKeyResponse): String {
        return gson.toJson(locationKeyResponse)
    }

    @TypeConverter
    fun toLocationKeyResponse(json: String): LocationKeyResponse {
        return gson.fromJson(json, LocationKeyResponse::class.java)
    }
}