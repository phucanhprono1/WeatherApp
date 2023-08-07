package com.example.weatherapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapp.response.FivedaysForecast.DailyForecast
import com.example.weatherapp.response.LocationInfo
import com.example.weatherapp.response.currentweather.CurrentWeatherResponseItem
import com.example.weatherapp.response.forecast24h.Forecast24hItem
import com.example.weatherapp.response.geolocation.LocationKeyResponse

@Database(entities = [CurrentWeatherResponseItem::class,DailyForecast::class,
    Forecast24hItem::class, LocationKeyResponse::class, LocationInfo::class], version = 1)
@TypeConverters(LocalDateTimeConverter::class, TimeZoneConverter::class,
    LocationKeyResponseConverter::class, CurrentWeatherResponseItemConverter::class,
DailyForecastConverter::class, Forecast24hItemConverter::class)
abstract class ForecastDatabase() : RoomDatabase(){
    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun futureWeatherDao(): FutureWeatherDao
    abstract fun hourlyForecastDao(): HourlyForecastDao
    abstract fun locationDao(): LocationDao
    abstract fun locationInfoDao(): LocationInfoDao

}