package ru.manzharovn.foodapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.manzharovn.foodapp.data.food.local.Food
import ru.manzharovn.foodapp.data.food.local.FoodDao


@Database(entities = [Food::class], version = 1, exportSchema = false)
abstract class FoodDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
}