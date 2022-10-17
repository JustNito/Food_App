package ru.manzharovn.foodapp.domain.usecase

import android.util.Log
import ru.manzharovn.foodapp.domain.model.ErrorHandler
import ru.manzharovn.foodapp.domain.repository.FoodRepository
import ru.manzharovn.foodapp.presentation.model.Food
import ru.manzharovn.foodapp.domain.model.Result
import javax.inject.Inject

class GetFoodUseCase @Inject constructor(
    val foodRepository: FoodRepository,
    val errorHandler: ErrorHandler
) {
    suspend operator fun invoke(): Result<List<Food>> = try {
        Result.Success(
            data = foodRepository.getFood().map {
                Food(
                    name = it.name,
                    description = it.description,
                    price = it.price,
                    imageUrl = it.imageUrl
                )
            }
        )
    } catch (e: Throwable) {
        Log.i("MainTest", e.message!!)
        throw e
        Result.Error(
            error = errorHandler.getError(e)
        )
    }
}