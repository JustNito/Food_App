package ru.manzharovn.foodapp.presentation.di

import dagger.Binds
import dagger.Module
import ru.manzharovn.foodapp.data.food.FoodRepositoryImpl
import ru.manzharovn.foodapp.data.utils.ErrorHandlerImpl
import ru.manzharovn.foodapp.domain.model.ErrorHandler
import ru.manzharovn.foodapp.domain.repository.FoodRepository

@Module
abstract class DomainModule {

    @Binds
    abstract fun provideErrorHandler(errorHandler: ErrorHandlerImpl): ErrorHandler

    @Binds
    abstract fun provideFoodRepository(foodRepositoryImpl: FoodRepositoryImpl): FoodRepository
}