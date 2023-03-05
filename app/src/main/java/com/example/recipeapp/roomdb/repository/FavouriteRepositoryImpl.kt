package com.example.recipeapp.roomdb.repository

import com.example.recipeapp.model.FavouriteData
import com.example.recipeapp.roomdb.dao.FavouriteDAO
import javax.inject.Inject

class FavouriteRepositoryImpl @Inject constructor(private val favouriteDAO: FavouriteDAO) {
    suspend fun getAllFavourites() = favouriteDAO.getAllFavourites()
    suspend fun insertItem(meal: FavouriteData) = favouriteDAO.insert(meal)
}