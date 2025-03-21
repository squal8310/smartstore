package org.smarts.smartbeerstore.data.petstore

import org.smarts.smartbeerstore.data.petstore.models.Pet
import org.smarts.smartbeerstore.data.petstore.models.PetStatus
import retrofit2.http.*

interface PetStoreApi {
    @GET("pet/{petId}")
    suspend fun getPetById(@Path("petId") petId: Long): Pet

    @GET("pet/findByStatus")
    suspend fun findPetsByStatus(@Query("status") status: PetStatus): List<Pet>

    @POST("pet")
    suspend fun addPet(@Body pet: Pet): Pet

    @PUT("pet")
    suspend fun updatePet(@Body pet: Pet): Pet

    @DELETE("pet/{petId}")
    suspend fun deletePet(@Path("petId") petId: Long)

    @GET("pet/findByTags")
    suspend fun findPetsByTags(@Query("tags") tags: List<String>): List<Pet>
} 