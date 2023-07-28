package com.example.weatherapp.di

import com.example.weatherapp.repository.LocationRepository
import com.example.weatherapp.repository.LocationRepositoryImpl
import com.example.weatherapp.room.CurrentWeatherDao
import com.example.weatherapp.room.ForecastDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object LocationModule {

//    @Provides
//    fun provideLocationDao(forecastDatabase: ForecastDatabase): LocationDao {
//        return forecastDatabase.locationDao()
//    }

    @Provides
    fun provideLocationRepo(): LocationRepository {
        return LocationRepositoryImpl()
    }
}