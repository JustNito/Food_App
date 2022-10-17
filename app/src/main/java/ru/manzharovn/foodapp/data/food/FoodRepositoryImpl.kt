package ru.manzharovn.foodapp.data.food

import ru.manzharovn.foodapp.data.food.local.FoodLocalDataSource
import ru.manzharovn.foodapp.data.food.remote.FoodRemoteDataSource
import ru.manzharovn.foodapp.domain.repository.FoodRepository
import ru.manzharovn.foodapp.domain.model.Food
import ru.manzharovn.foodapp.data.food.local.Food as LocalFood
import javax.inject.Inject

class FoodRepositoryImpl @Inject constructor(
    val foodRemoteDataSource: FoodRemoteDataSource,
    val foodLocalDataSource: FoodLocalDataSource
) : FoodRepository {
    override suspend fun getFood(): List<Food> {
        if(isDataBaseEmpty()) {
            val food = foodRemoteDataSource.getFood().data.map {
                LocalFood(
                    name = it.name,
                    description = it.description,
                    price = it.price,
                    imageUrl = it.imageUrl
                )
            }
            foodLocalDataSource.insertFood(food)
            return food.map {
                Food(
                    name = it.name,
                    description = it.description,
                    imageUrl = it.imageUrl,
                    price = it.price,
                )
            }
        } else {
            return foodLocalDataSource.getFood().map {
                Food(
                    name = it.name,
                    description = it.description,
                    imageUrl = it.imageUrl,
                    price = it.price,
                )
            }
        }
    }

    private suspend fun isDataBaseEmpty() = foodLocalDataSource.getOneIfNotEmptyElseZero() == 0
}