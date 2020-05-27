package com.luciburcugithub.mobilebackend.repo

import android.util.Log
import com.google.gson.GsonBuilder
import com.luciburcugithub.mobilebackend.model.CityEntity
import com.luciburcugithub.mobilebackend.retrofit.CityService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CityRepository {

    private var service: CityService = Retrofit.Builder().baseUrl("http:192.168.0.104:8080")
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create())).build()
        .create(CityService::class.java)

    suspend fun getCities(): List<CityEntity> {
        return try {
            service.getCities()
        } catch (_: Exception) {
            emptyList()
        }
    }

    suspend fun createCity(cityEntity: CityEntity) {
        try {
            service.createCity(cityEntity)
        } catch (_: Exception) {
            Log.e("ERROR", "Error encountered")
        }
    }

    suspend fun updateCity(id: Int, cityEntity: CityEntity) {
        try {
            service.updateCity(id, cityEntity)
        } catch (_: Exception) {
            Log.e("ERROR", "Error encountered")
        }
    }

    suspend fun deleteCity(id: Int) {
        try {
            service.deleteCity(id)
        } catch (_: Exception) {
            Log.e("ERROR", "Error encountered")
        }
    }

}