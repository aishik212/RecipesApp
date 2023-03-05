package com.example.recipeapp.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.recipeapp.model.db.FavouriteData
import com.example.recipeapp.model.db.ShoppingItemsData
import com.example.recipeapp.model.db.WeeklyShoppingItemsData
import com.example.recipeapp.roomdb.dao.FavouriteDAO
import com.example.recipeapp.roomdb.dao.ShoppingListDAO
import com.example.recipeapp.roomdb.dao.WeeklyShoppingListDAO

@Database(
    entities = [FavouriteData::class, ShoppingItemsData::class, WeeklyShoppingItemsData::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouriteDAO(): FavouriteDAO
    abstract fun weeklyShoppingListDAO(): WeeklyShoppingListDAO
    abstract fun shoppingListDAO(): ShoppingListDAO
}