package ru.manzharovn.foodapp.presentation.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.manzharovn.foodapp.presentation.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, LocalModule::class, DomainModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(activity: MainActivity)
}