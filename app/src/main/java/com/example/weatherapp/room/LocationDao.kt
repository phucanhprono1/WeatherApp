package com.example.weatherapp.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.response.geolocation.LocationKeyResponse


@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocation(location : LocationKeyResponse)

    @Delete
    fun deleteLocation(location: LocationKeyResponse)

    @Query("select * FROM location_key")
    fun getAllLocation(): List<LocationKeyResponse>
    @Query("select * FROM location_key")
    fun getAllLocationLive(): LiveData<List<LocationKeyResponse>>

}