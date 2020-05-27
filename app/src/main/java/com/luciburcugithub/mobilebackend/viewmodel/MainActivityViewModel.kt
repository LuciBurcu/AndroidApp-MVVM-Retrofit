package com.luciburcugithub.mobilebackend.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.luciburcugithub.mobilebackend.model.CityEntity
import com.luciburcugithub.mobilebackend.repo.CityRepository
import kotlinx.coroutines.launch

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private var repo = CityRepository()
    var cityLiveData: MutableLiveData<List<CityEntity>> = MutableLiveData()

    // variable used to simulate the ID behaviour for backend
    var counterID = 0

    fun getId(): Int {
        return counterID++
    }

    init {
        viewModelScope.launch {
            cityLiveData.value = repo.getCities()
            counterID = cityLiveData.value?.size!!
        }
    }

    fun getCities() = viewModelScope.launch {
        cityLiveData.value = repo.getCities()
    }

    fun createCity(cityEntity: CityEntity) = viewModelScope.launch {
        repo.createCity(cityEntity)
        cityLiveData.value = repo.getCities()
    }

    fun deleteCity(id: Int) = viewModelScope.launch {
        repo.deleteCity(id)
        cityLiveData.value = repo.getCities()
    }

    fun updateCity(id: Int, cityEntity: CityEntity) = viewModelScope.launch {
        repo.updateCity(id, cityEntity)
        cityLiveData.value = repo.getCities()

    }

}
