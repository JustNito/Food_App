package ru.manzharovn.foodapp.data.food.remote

import ru.manzharovn.foodapp.data.network.FoodApi
import javax.inject.Inject

class FoodRemoteDataSource @Inject constructor(val foodApi: FoodApi) {
    suspend fun getFood() = foodApi.retrofitService.getFood()
}