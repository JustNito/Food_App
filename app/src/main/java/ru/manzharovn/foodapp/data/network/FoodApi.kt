package ru.manzharovn.foodapp.data.network

import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import ru.manzharovn.foodapp.data.food.remote.Food
import ru.manzharovn.foodapp.data.utils.FoodResponse
import javax.inject.Inject

class FoodApi @Inject constructor(val retrofit: Retrofit) {

    interface FoodApiService {
        @GET("productos")
        suspend fun getFood(): FoodResponse
    }

    val retrofitService: FoodApiService by lazy {
        retrofit.create(FoodApiService::class.java)
    }
}