package com.example.weatherapp.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.response.LocationInfo

@Dao
interface LocationInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocation(location : LocationInfo)

    @Delete
    fun deleteLocation(location: LocationInfo)

    @Query("select * FROM location_info")
    fun getAllLocation(): List<LocationInfo>
}