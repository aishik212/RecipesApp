package com.example.recipeapp.roomdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipeapp.roomdb.FavouriteData

@Dao
interface FavouriteDAO {

    @Query("SELECT * FROM favouriteData")
    fun getAllFavourites(): List<FavouriteData>

    @Query("SELECT COUNT(*) FROM favouriteData")
    fun getFavouriteCount(): Int

    @Query("SELECT COUNT(*) FROM favouriteData where id = :idofRecipe")
    fun getisInFavourite(idofRecipe: String?): Int


    @Query("DELETE FROM favouriteData where id = :idofRecipe")
    fun deleteFavourite(idofRecipe: String?): Int


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(recipeData: FavouriteData)
}