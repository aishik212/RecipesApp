package com.example.recipeapp.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeeklyShoppingItemsData(


    @ColumnInfo(name = "items")
    var items: String? = "",

    @ColumnInfo(name = "day")
    var day: String? = ""
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
}
