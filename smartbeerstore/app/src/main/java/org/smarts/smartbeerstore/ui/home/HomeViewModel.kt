package org.smarts.smartbeerstore.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.smarts.smartbeerstore.data.weather.Cities
import org.smarts.smartbeerstore.data.weather.WeatherData
import org.smarts.smartbeerstore.data.weather.WeatherRepository
import java.io.IOException

class HomeViewModel : ViewModel() {
    private val repository = WeatherRepository()
    private val _weatherData = MutableLiveData<WeatherData?>()
    val weatherData: LiveData<WeatherData?> = _weatherData

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _selectedCity = MutableLiveData<String>()
    val selectedCity: LiveData<String> = _selectedCity

    init {
        _selectedCity.value = Cities.cities.first()
        loadWeather()
    }

    fun setSelectedCity(city: String) {
        _selectedCity.value = city
        loadWeather()
    }

    fun loadWeather() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _weatherData.value = null // Clear previous data while loading
                val weather = repository.getWeather(_selectedCity.value ?: Cities.cities.first())
                _weatherData.value = weather
            } catch (e: IOException) {
                _error.value = e.message
                _weatherData.value = null
            } catch (e: Exception) {
                _error.value = "An unexpected error occurred: ${e.message}"
                _weatherData.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}