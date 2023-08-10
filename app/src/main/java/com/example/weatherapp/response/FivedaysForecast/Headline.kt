package com.example.weatherapp.response.FivedaysForecast
//
//import androidx.room.Entity
//
//@Entity(tableName = "headline")
data class Headline(
    val Category: String,
    val EffectiveDate: String,
    val EffectiveEpochDate: Int,
    val EndDate: String,
    val EndEpochDate: Int,
    val Link: String,
    val MobileLink: String,
    val Severity: Int,
    val Text: String
)