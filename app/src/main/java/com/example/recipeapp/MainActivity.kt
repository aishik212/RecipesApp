package com.example.recipeapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.recipeapp.activity.FavoriteFoodsActivity
import com.example.recipeapp.activity.ShopListActivity
import com.example.recipeapp.activity.UserProfileActivity
import com.example.recipeapp.adapter.HomePageFoodAdapter
import com.example.recipeapp.databinding.ActivityMainBinding
import com.example.recipeapp.model.Meal
import com.example.recipeapp.model.MealModel
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response


class MainActivity : AppCompatActivity() {
    lateinit var inflate: ActivityMainBinding
    lateinit var homePageFoodAdapter: HomePageFoodAdapter
    val mealList: MutableList<Meal> = mutableListOf()
    lateinit var client: OkHttpClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflate = ActivityMainBinding.inflate(layoutInflater)
        homePageFoodAdapter = HomePageFoodAdapter(mealList, this)
        setContentView(inflate.root)
        val recyclerView = inflate.recyclerView
        recyclerView.adapter = homePageFoodAdapter
        client = OkHttpClient().newBuilder()
            .build()
        initialCall()
        inflate.searchEt.doAfterTextChanged {
            searchMeal(it.toString())
            Log.d("texts", "onCreate: " + it.toString())
        }
        inflate.floatingActionButton.setOnClickListener {
            val intent = Intent(this, FavoriteFoodsActivity::class.java)
            startActivity(intent)
        }
        inflate.shopListBtn.setOnClickListener {
            val intent = Intent(this, ShopListActivity::class.java)
            startActivity(intent)
        }
        inflate.userProfile.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }
        if (BuildConfig.DEBUG) {
            /* val intent = Intent(this, UserProfileActivity::class.java)
             startActivity(intent)*/
            /*val intent = Intent(this, FoodDetailsActivity::class.java)
            intent.putExtra("id", "52772")
            startActivity(intent)*/
        }
    }

    private fun initialCall() {
        val request: Request = Request.Builder()
            .url("https://www.themealdb.com/api/json/v1/1/filter.php?c=chicken")
            .method("GET", null)
            .build()
        fetchData(request)
    }

    private fun searchMeal(s: String) {
        if (s.isNotEmpty()) {
            val request: Request = Request.Builder()
                .url("https://www.themealdb.com/api/json/v1/1/search.php?s=$s")
                .method("GET", null)
                .build()
            fetchData(request)
        } else {
            initialCall()
        }
    }

    private fun fetchData(request: Request) {
        CoroutineScope(Dispatchers.IO).launch {
            val response: Response = client.newCall(request).execute()
            try {
                val mealModel: MealModel =
                    Gson().fromJson(response.body?.string(), MealModel::class.java)
                updateList(mealModel)
            } catch (_: Exception) {
            }
        }
    }

    private fun updateList(mealModel: MealModel) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                mealList.clear()
                mealList.addAll(mealModel.meals)
            } catch (_: Exception) {
            }

            if (mealList.isEmpty()) {
                initialCall()
                Toast.makeText(applicationContext, "No Items Found", Toast.LENGTH_SHORT).show()
            } else {
                homePageFoodAdapter.notifyDataSetChanged()
            }
        }
    }
}