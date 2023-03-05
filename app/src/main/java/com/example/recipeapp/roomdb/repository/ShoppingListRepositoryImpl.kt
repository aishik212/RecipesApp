package com.example.recipeapp.roomdb.repository

import com.example.recipeapp.model.ShoppingItemsData
import com.example.recipeapp.roomdb.dao.ShoppingListDAO
import javax.inject.Inject

class ShoppingListRepositoryImpl @Inject constructor(private val shoppingListDAO: ShoppingListDAO) {
    suspend fun getAllShoppingList() = shoppingListDAO.getAllShoppingList()
    suspend fun insertItem(meal: ShoppingItemsData) = shoppingListDAO.insert(meal)
}