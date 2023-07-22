package com.example.weatherapp.di

import com.example.weatherapp.repository.ForecastRepository
import com.example.weatherapp.repository.ForecastRepositoryImpl
import com.example.weatherapp.room.CurrentWeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // Cung cấp ForecastRepository


}