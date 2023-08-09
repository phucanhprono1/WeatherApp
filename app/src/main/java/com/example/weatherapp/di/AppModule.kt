package com.example.weatherapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.lifecycle.LifecycleOwner
import androidx.room.Room
import com.example.weatherapp.api.ServiceFactory
import com.example.weatherapp.ui.fragment.currentweather.CurrentWeatherFragment
import com.example.weatherapp.ui.fragment.currentweather.CurrentWeatherViewModelFactory
import com.example.weatherapp.networkdata.WeatherNetworkDataSource
import com.example.weatherapp.networkdata.WeatherNetworkDataSourceImpl
import com.example.weatherapp.repository.ForecastRepository
import com.example.weatherapp.repository.ForecastRepositoryImpl
import com.example.weatherapp.repository.LocationRepository
import com.example.weatherapp.room.CurrentWeatherDao
import com.example.weatherapp.room.ForecastDatabase
import com.example.weatherapp.room.FutureWeatherDao
import com.example.weatherapp.room.HourlyForecastDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.FragmentScoped
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }
    @Provides
    @Singleton
    fun provideLifecycleOwner(): LifecycleOwner {
        // Provide an instance of LifecycleOwner, you can use your activity or fragment here
        // For example, if you are in an Activity, you can return 'this'.
        // If you are in a Fragment, you can return 'viewLifecycleOwner'.
        return provideCurrentWeatherFragment()
    }
    @Provides
    @FragmentScoped
    fun provideCurrentWeatherFragment(): CurrentWeatherFragment {
        return CurrentWeatherFragment.newInstance("215854")
    }
    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }
    @Provides
    fun provideHourlyForecastDao(forecastDatabase: ForecastDatabase): HourlyForecastDao {
        return forecastDatabase.hourlyForecastDao()
    }
    @Provides
    fun provideForecastDatabase(context: Context): ForecastDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ForecastDatabase::class.java, "accuweathe.db"
        ).allowMainThreadQueries()
            .build()
    }
    @Provides
    fun provideCurrentWeatherDao(forecastDatabase: ForecastDatabase): CurrentWeatherDao {
        return forecastDatabase.currentWeatherDao()
    }
    @Provides
    fun provideFutureWeatherDao(forecastDatabase: ForecastDatabase): FutureWeatherDao {
        return forecastDatabase.futureWeatherDao()
    }
    @Provides
    fun provideServiceFactory(): ServiceFactory {
        return ServiceFactory // Assuming ServiceFactory is a singleton or has a static method to create a singleton instance
    }
    @Provides
    fun provideWeatherNetworkDataSource(serviceFactory: ServiceFactory): WeatherNetworkDataSource {
        return WeatherNetworkDataSourceImpl(serviceFactory)
    }

    @Provides
    fun provideForecastRepository(
        currentWeatherDao: CurrentWeatherDao,
        weatherNetworkDataSource: WeatherNetworkDataSource,
        sharedPreferences: SharedPreferences,
        futureWeatherDao: FutureWeatherDao,
        hourlyForecastDao: HourlyForecastDao,
        locationRepository: LocationRepository
    ): ForecastRepository {
        return ForecastRepositoryImpl(currentWeatherDao, weatherNetworkDataSource, sharedPreferences, futureWeatherDao, hourlyForecastDao , locationRepository)
    }
    @Provides
    fun provideCurrentWeatherViewModelFactory(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        forecastRepository: ForecastRepository
    ): CurrentWeatherViewModelFactory {
        return CurrentWeatherViewModelFactory(context, lifecycleOwner, forecastRepository)
    }
}