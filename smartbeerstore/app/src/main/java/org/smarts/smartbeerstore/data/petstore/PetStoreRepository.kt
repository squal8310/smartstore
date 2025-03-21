package org.smarts.smartbeerstore.data.petstore

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.smarts.smartbeerstore.data.petstore.models.Pet
import org.smarts.smartbeerstore.data.petstore.models.PetStatus
import java.io.IOException

class PetStoreRepository {
    private val api: PetStoreApi

    init {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://petstore.swagger.io/v2/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(PetStoreApi::class.java)
    }

    suspend fun getAvailablePets(): List<Pet> {
        try {
            return api.findPetsByStatus(PetStatus.AVAILABLE)
        } catch (e: Exception) {
            throw IOException("Error fetching pets: ${e.message}")
        }
    }

    suspend fun getPetById(id: Long): Pet {
        try {
            return api.getPetById(id)
        } catch (e: Exception) {
            throw IOException("Error fetching pet: ${e.message}")
        }
    }

    suspend fun addPet(pet: Pet): Pet {
        try {
            return api.addPet(pet)
        } catch (e: Exception) {
            throw IOException("Error adding pet: ${e.message}")
        }
    }

    suspend fun updatePet(pet: Pet): Pet {
        try {
            return api.updatePet(pet)
        } catch (e: Exception) {
            throw IOException("Error updating pet: ${e.message}")
        }
    }

    suspend fun deletePet(id: Long) {
        try {
            api.deletePet(id)
        } catch (e: Exception) {
            throw IOException("Error deleting pet: ${e.message}")
        }
    }
} 