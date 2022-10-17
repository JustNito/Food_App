package ru.manzharovn.foodapp.data.food.remote

import com.squareup.moshi.Json

data class Food (
    @Json(name = "nombre")
    val name: String,
    @Json(name = "descripcion")
    val description: String?,
    @Json(name = "precio")
    val price: Int?,
    @Json(name = "linkImagen")
    val imageUrl: String?,
)