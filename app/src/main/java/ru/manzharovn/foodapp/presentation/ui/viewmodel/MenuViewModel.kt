package ru.manzharovn.foodapp.presentation.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.ImageResult
import kotlinx.coroutines.launch
import ru.manzharovn.foodapp.domain.model.ErrorEntity
import ru.manzharovn.foodapp.domain.usecase.GetFoodUseCase
import ru.manzharovn.foodapp.presentation.model.Food
import ru.manzharovn.foodapp.domain.model.Result
import ru.manzharovn.foodapp.presentation.utils.Status

class MenuViewModel(
    private val getFoodUseCase: GetFoodUseCase,
    val imageLoader: ImageLoader
) : ViewModel() {

    private var _selectedCity by mutableStateOf("Москва")
    val selectedCity: String
        get() = _selectedCity

    private var _categories = listOf("пицца", "бургеры", "напитки", "суши", "вок", "десерт")
    val categories: List<String>
        get() = _categories

    private var _currentCategory by mutableStateOf(categories.first())
    val currentCategory
        get() = _currentCategory

    private val _food = mutableStateListOf<Food>()
    val food: List<Food>
        get() = _food

    private var _status by mutableStateOf(Status.LOADING)
    val status: Status
        get() = _status

    init {
        getFood()
    }

    fun tryAgain() {
        getFood()
    }

    private fun getFood() = viewModelScope.launch {
        _status = Status.LOADING
        when(val result = getFoodUseCase()) {
            is Result.Success -> {
                _food.addAll(result.data)
                _status = Status.OK
            }
            is Result.Error -> {
                changeStatusByError(result.error)
            }
        }
    }

    fun onCategoryClick(category: String) {
        if(category != _currentCategory) {
            _currentCategory = category
        }
    }

    fun onCityChange(city: String) {
        _selectedCity = city
    }

    private fun changeStatusByError(error: ErrorEntity) {
        _status = when (error) {
            ErrorEntity.Unknown -> Status.UNKNOWN
            ErrorEntity.AccessDenied -> Status.ACCESS_DENIED
            ErrorEntity.ServiceUnavailable -> Status.UNAVAILABLE
            ErrorEntity.Network -> Status.NETWORK
            ErrorEntity.CoroutineCancel -> Status.UNKNOWN
            ErrorEntity.NotFound -> Status.NOT_FOUND
        }
    }
}