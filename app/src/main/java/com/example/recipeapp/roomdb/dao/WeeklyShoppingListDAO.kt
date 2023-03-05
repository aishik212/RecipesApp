package com.example.recipeapp.roomdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipeapp.model.db.WeeklyShoppingItemsData

@Dao
interface WeeklyShoppingListDAO {

    @Query("SELECT * FROM WeeklyShoppingItemsData")
    fun getAllWeeklyShoppingList(): List<WeeklyShoppingItemsData>


    @Query("SELECT * FROM WeeklyShoppingItemsData where day = :idofRecipe")
    fun getWeeklDayShoppingList(idofRecipe: String): List<WeeklyShoppingItemsData>

    @Query("SELECT COUNT(*) FROM WeeklyShoppingItemsData")
    fun getWeeklyShoppingListCount(): Int

    @Query("SELECT COUNT(*) FROM WeeklyShoppingItemsData where id = :idofRecipe")
    fun exists(idofRecipe: String?): Int


    @Query("DELETE FROM WeeklyShoppingItemsData where id = :idofRecipe")
    fun delete(idofRecipe: Long?): Int


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipeData: WeeklyShoppingItemsData)
}