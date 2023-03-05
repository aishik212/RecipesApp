package com.example.recipeapp.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.recipeapp.model.FavouriteData
import com.example.recipeapp.model.ShoppingItemsData
import com.example.recipeapp.roomdb.dao.FavouriteDAO
import com.example.recipeapp.roomdb.dao.ShoppingListDAO

@Database(
    entities = [FavouriteData::class, ShoppingItemsData::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouriteDAO(): FavouriteDAO
    abstract fun shoppingListDAO(): ShoppingListDAO
}