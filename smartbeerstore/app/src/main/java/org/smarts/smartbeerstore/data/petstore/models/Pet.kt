package org.smarts.smartbeerstore.data.petstore.models

data class Pet(
    val id: Long? = null,
    val category: Category? = null,
    val name: String,
    val photoUrls: List<String>,
    val tags: List<Tag>? = null,
    val status: PetStatus? = null
)

data class Category(
    val id: Long? = null,
    val name: String? = null
)

data class Tag(
    val id: Long? = null,
    val name: String? = null
)

enum class PetStatus {
    AVAILABLE,
    PENDING,
    SOLD
} 