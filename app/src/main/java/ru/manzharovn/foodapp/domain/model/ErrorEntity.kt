package ru.manzharovn.foodapp.domain.model

interface ErrorHandler {

    fun getError(throwable: Throwable): ErrorEntity
}

sealed class ErrorEntity {

    object Network: ErrorEntity()

    object CoroutineCancel: ErrorEntity()

    object NotFound: ErrorEntity()

    object Unknown: ErrorEntity()

    object AccessDenied : ErrorEntity()

    object ServiceUnavailable : ErrorEntity()
}
