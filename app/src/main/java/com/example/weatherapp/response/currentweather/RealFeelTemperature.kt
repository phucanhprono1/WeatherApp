package com.example.weatherapp.response.currentweather

import androidx.room.Embedded

import com.google.gson.annotations.SerializedName

data class RealFeelTemperature(
    @SerializedName("Imperial")
    @Embedded(prefix = "imperial_")
    val Imperial: ImperialRealFeel,
    @SerializedName("Metric")
    @Embedded(prefix = "metric_")
    val Metric: MetricRealFeel
)