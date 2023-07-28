package com.example.weatherapp.response.geolocation

import androidx.room.Embedded
import com.example.weatherapp.response.geolocation.Elevation

data class GeoPosition(
    @Embedded
    val Elevation: Elevation,
    val Latitude: Double,
    val Longitude: Double
)