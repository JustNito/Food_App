package ru.manzharovn.foodapp.data.food.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FoodDao {

    @Query("SELECT * FROM food")
    suspend fun getFood(): List<Food>

    @Query("select exists(select 1 from food)")
    suspend fun getOneIfNotEmptyElseZero(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: List<Food>)
}