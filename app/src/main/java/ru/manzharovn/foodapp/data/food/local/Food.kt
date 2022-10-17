package ru.manzharovn.foodapp.data.food.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food")
data class Food (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String?,
    val price: Int?,
    val imageUrl: String?,
)