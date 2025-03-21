package org.smarts.smartbeerstore.ui.petstore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.smarts.smartbeerstore.data.petstore.PetStoreRepository
import org.smarts.smartbeerstore.data.petstore.models.Pet
import java.io.IOException

class PetStoreViewModel : ViewModel() {
    private val repository = PetStoreRepository()
    private val _pets = MutableLiveData<List<Pet>>()
    val pets: LiveData<List<Pet>> = _pets

    private val _selectedPet = MutableLiveData<Pet?>()
    val selectedPet: LiveData<Pet?> = _selectedPet

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        loadAvailablePets()
    }

    fun loadAvailablePets() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val availablePets = repository.getAvailablePets()
                _pets.value = availablePets
            } catch (e: IOException) {
                _error.value = e.message
                _pets.value = emptyList()
            } catch (e: Exception) {
                _error.value = "An unexpected error occurred: ${e.message}"
                _pets.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectPet(pet: Pet) {
        _selectedPet.value = pet
    }

    fun clearSelectedPet() {
        _selectedPet.value = null
    }

    fun addPet(pet: Pet) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                repository.addPet(pet)
                loadAvailablePets() // Reload the list
            } catch (e: IOException) {
                _error.value = e.message
            } catch (e: Exception) {
                _error.value = "An unexpected error occurred: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updatePet(pet: Pet) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                repository.updatePet(pet)
                loadAvailablePets() // Reload the list
            } catch (e: IOException) {
                _error.value = e.message
            } catch (e: Exception) {
                _error.value = "An unexpected error occurred: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deletePet(id: Long) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                repository.deletePet(id)
                loadAvailablePets() // Reload the list
            } catch (e: IOException) {
                _error.value = e.message
            } catch (e: Exception) {
                _error.value = "An unexpected error occurred: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
} 