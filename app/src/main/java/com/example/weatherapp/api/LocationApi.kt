package com.example.weatherapp.api

import com.example.weatherapp.response.geolocation.LocationKeyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApi {
    @GET("locations/v1/cities/geoposition/search")
    suspend fun getLocationKey(
        @Query("apikey") apiKey: String,
        @Query("q") location: String
    ): Response<LocationKeyResponse>

    @GET("locations/v1/cities/search")
    suspend fun getLocationByName(
        @Query("apikey") apiKey: String,
        @Query("q") location: String
    ) : Response<List<LocationKeyResponse>>
}