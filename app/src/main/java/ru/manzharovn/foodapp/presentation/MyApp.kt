package ru.manzharovn.foodapp.presentation

import android.app.Application
import ru.manzharovn.foodapp.presentation.di.DaggerAppComponent

class MyApp : Application() {
    val appComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}