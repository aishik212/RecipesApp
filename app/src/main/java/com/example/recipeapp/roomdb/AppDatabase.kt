package com.example.recipeapp.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.recipeapp.roomdb.dao.FavouriteDAO

@Database(entities = [FavouriteData::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouriteDAO(): FavouriteDAO
}