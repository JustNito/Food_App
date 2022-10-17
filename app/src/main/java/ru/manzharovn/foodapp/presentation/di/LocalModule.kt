package ru.manzharovn.foodapp.presentation.di

import android.content.Context
import androidx.room.Room
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import dagger.Module
import dagger.Provides
import ru.manzharovn.foodapp.data.db.FoodDatabase
import ru.manzharovn.foodapp.data.food.local.FoodDao
import javax.inject.Singleton

@Module
class LocalModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context): FoodDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            FoodDatabase::class.java,
            "food_database"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideFoodDao(db: FoodDatabase): FoodDao {
        return db.foodDao()
    }

    @Singleton
    @Provides
    fun provideImageLoader(context: Context): ImageLoader =
        ImageLoader.Builder(context)
            .memoryCache {
                MemoryCache.Builder(context)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.02)
                    .build()
            }
            .build()

}