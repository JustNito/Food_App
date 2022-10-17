package ru.manzharovn.foodapp.data.utils

import com.squareup.moshi.Json
import ru.manzharovn.foodapp.data.food.remote.Food

data class FoodResponse(
    @Json(name = "productos")
    val data: List<Food>
)