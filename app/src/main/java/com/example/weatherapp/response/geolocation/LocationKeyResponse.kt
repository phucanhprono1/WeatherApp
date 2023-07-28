package com.example.weatherapp.response.geolocation

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_key")
data class LocationKeyResponse(
//    val AdministrativeArea: AdministrativeArea,
//    val Country: Country,
//    val DataSets: List<String>,
    val EnglishName: String,
//    val GeoPosition: GeoPosition,
    val IsAlias: Boolean,
    @PrimaryKey
    val Key: String,
    val LocalizedName: String,
//    @Embedded(prefix = "ParentCity_")
//    val ParentCity: ParentCity,
    val PrimaryPostalCode: String,
    val Rank: Int,
//    @Embedded(prefix = "Region_")
//    val Region: Region,
//    val SupplementalAdminAreas: List<Any>,
    @Embedded(prefix = "TimeZone_")
    val TimeZone: TimeZone,
    val Type: String,
    val Version: Int
)