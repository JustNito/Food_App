package ru.manzharovn.foodapp.domain.repository

import ru.manzharovn.foodapp.domain.model.Food

interface FoodRepository {

    suspend fun getFood(): List<Food>
}