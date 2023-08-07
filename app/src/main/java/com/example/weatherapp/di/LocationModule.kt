package com.example.weatherapp.di

import com.example.weatherapp.repository.LocationRepository
import com.example.weatherapp.repository.LocationRepositoryImpl
import com.example.weatherapp.room.CurrentWeatherDao
import com.example.weatherapp.room.ForecastDatabase
import com.example.weatherapp.room.LocationDao
import com.example.weatherapp.room.LocationInfoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object LocationModule {

    @Provides
    fun provideLocationDao(forecastDatabase: ForecastDatabase): LocationDao {
        return forecastDatabase.locationDao()
    }
    @Provides
    fun provideLocationRepo(locationDao: LocationDao, infoDao: LocationInfoDao): LocationRepository {
        return LocationRepositoryImpl(locationDao,infoDao)
    }
    @Provides
    fun provideLocationInfoDao(forecastDatabase: ForecastDatabase) : LocationInfoDao{
        return forecastDatabase.locationInfoDao()
    }
}