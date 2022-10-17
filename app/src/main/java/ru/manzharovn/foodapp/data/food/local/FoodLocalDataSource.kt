package ru.manzharovn.foodapp.data.food.local

import javax.inject.Inject

class FoodLocalDataSource @Inject constructor(val foodDao: FoodDao) {
    suspend fun getFood(): List<Food> = foodDao.getFood()
    suspend fun insertFood(food: List<Food>) = foodDao.insertFood(food)
    suspend fun getOneIfNotEmptyElseZero() = foodDao.getOneIfNotEmptyElseZero()
}