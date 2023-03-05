package com.example.recipeapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.recipeapp.adapter.HomePageFoodAdapter
import com.example.recipeapp.databinding.FavouriteFoodLayoutBinding
import com.example.recipeapp.model.Meal
import com.example.recipeapp.roomdb.dao.FavouriteDAO
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFoodsActivity : AppCompatActivity() {


    @Inject
    lateinit var favouriteDAO: FavouriteDAO
    lateinit var homePageFoodAdapter: HomePageFoodAdapter
    val mealList: MutableList<Meal> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflate = FavouriteFoodLayoutBinding.inflate(layoutInflater)
        setContentView(inflate.root)
        val recyclerView = inflate.recyclerView
        homePageFoodAdapter = HomePageFoodAdapter(mealList, this@FavoriteFoodsActivity)
        recyclerView.adapter = homePageFoodAdapter
        CoroutineScope(Dispatchers.IO).launch {
            mealList.clear()
            favouriteDAO.getAllFavourites().iterator().forEach {
                val element = it.meal
                val meal = Gson().fromJson(element, Meal::class.java)
                mealList.add(meal)
            }
            CoroutineScope(Dispatchers.Main).launch {
                homePageFoodAdapter.notifyDataSetChanged()
            }
        }

    }
}
