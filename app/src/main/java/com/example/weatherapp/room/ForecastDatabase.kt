package com.example.weatherapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapp.response.FivedaysForecast.DailyForecast
import com.example.weatherapp.response.currentweather.CurrentWeatherResponseItem

@Database(entities = [CurrentWeatherResponseItem::class,DailyForecast::class], version = 1)
@TypeConverters(LocalDateConverter::class)
abstract class ForecastDatabase() : RoomDatabase(){
    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun futureWeatherDao(): FutureWeatherDao

//    companion object {
//        @Volatile private var instance: ForecastDatabase? = null
//        private val LOCK = Any()
//        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
//            instance ?: buildDatabase(context).also { instance = it }
//        }
//
//        private fun buildDatabase(context: Context) =
//            Room.databaseBuilder(
//                context.applicationContext,
//                ForecastDatabase::class.java, "accuwea.db"
//            )
//            .build()
//
//    }

}