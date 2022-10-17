package ru.manzharovn.foodapp.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import ru.manzharovn.foodapp.domain.usecase.GetFoodUseCase
import javax.inject.Inject

class MenuViewModelFactory @Inject constructor(
    val getFoodUseCase: GetFoodUseCase,
    val imageLoader: ImageLoader
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MenuViewModel(
            getFoodUseCase = getFoodUseCase,
            imageLoader = imageLoader
        ) as T
    }
}