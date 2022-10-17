package ru.manzharovn.foodapp.domain.model

data class Food (
    val name: String,
    val description: String?,
    val price: Int?,
    val imageUrl: String?,
)