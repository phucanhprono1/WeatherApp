package com.example.weatherapp.response.geolocation

import com.example.weatherapp.response.geolocation.Elevation

data class GeoPosition(
    val Elevation: Elevation,
    val Latitude: Double,
    val Longitude: Double
)