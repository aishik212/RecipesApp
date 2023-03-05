package com.example.recipeapp.roomdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipeapp.model.ShoppingItemsData

@Dao
interface ShoppingListDAO {

    @Query("SELECT * FROM ShoppingItemsData")
    fun getAllShoppingList(): List<ShoppingItemsData>

    @Query("SELECT COUNT(*) FROM ShoppingItemsData")
    fun getShoppingListCount(): Int

    @Query("SELECT COUNT(*) FROM ShoppingItemsData where id = :idofRecipe")
    fun exists(idofRecipe: String?): Int


    @Query("DELETE FROM ShoppingItemsData where id = :idofRecipe")
    fun delete(idofRecipe: Long?): Int


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipeData: ShoppingItemsData)
}