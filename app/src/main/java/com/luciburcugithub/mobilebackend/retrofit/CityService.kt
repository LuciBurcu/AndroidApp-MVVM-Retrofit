package com.luciburcugithub.mobilebackend.retrofit

import com.luciburcugithub.mobilebackend.model.CityEntity
import retrofit2.http.*

interface CityService {

    @GET("/")
    suspend fun getCities(): List<CityEntity>

    @POST("/create")
    suspend fun createCity(@Body cityEntity: CityEntity)

    @DELETE("/delete/{id}")
    suspend fun deleteCity(@Path("id") id: Int)

    @PUT("/update/{id}")
    suspend fun updateCity(@Path("id") id: Int, @Body cityEntity: CityEntity)

}