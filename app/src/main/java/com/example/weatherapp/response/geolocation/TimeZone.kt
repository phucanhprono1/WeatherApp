package com.example.weatherapp.response.geolocation

data class TimeZone(
    val Code: String,
    val GmtOffset: Double,
    val IsDaylightSaving: Boolean,
    val Name: String,
//    val NextOffsetChange: Any? = null
)