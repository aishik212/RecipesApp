package com.example.recipeapp.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavouriteData(
    @PrimaryKey
    var id: Long? = null,

    @ColumnInfo(name = "meal")
    var meal: String? = ""
)
