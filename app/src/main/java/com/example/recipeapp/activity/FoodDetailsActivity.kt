package com.example.recipeapp.activity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.databinding.FoodDetailsLayoutBinding
import com.example.recipeapp.roomdb.FavouriteData
import com.example.recipeapp.roomdb.dao.FavouriteDAO
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class FoodDetailsActivity : AppCompatActivity() {
    lateinit var inflate: FoodDetailsLayoutBinding

    @Inject
    lateinit var favouriteDAO: FavouriteDAO

    var id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflate = FoodDetailsLayoutBinding.inflate(layoutInflater)
        setContentView(inflate.root)
        id = intent.extras?.getString("id", null)
        val client = OkHttpClient().newBuilder()
            .build()
        if (id != null) {
            initialCall(client, id)
        } else {
            finish()
        }

        CoroutineScope(Dispatchers.IO).launch {
            favouriteDAO.getAllFavourites().forEach {
                Log.d("texts", "onCreate: " + it)
            }

            Log.d("texts", "onCreate: " + favouriteDAO.getFavouriteCount())

            updateFavourites()


        }
    }

    private fun updateFavourites() {
        CoroutineScope(Dispatchers.IO).launch {
            if (id != null) {
                if (favouriteDAO.getisInFavourite(id) == 0) {
                    inflate.addFav.setImageDrawable(
                        ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.baseline_favorite_border_24
                        )
                    )
                } else {
                    inflate.addFav.setImageDrawable(
                        ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.baseline_favorite_24
                        )
                    )
                }
            }

        }
    }

    private fun initialCall(client: OkHttpClient, id: String?) {
        id?.let {
            val request: Request = Request.Builder()
                .url("https://www.themealdb.com/api/json/v1/1/lookup.php?i=$id")
                .method("GET", null)
                .build()
            fetchData(client, request)
        }
    }


    private fun fetchData(client: OkHttpClient, request: Request) {
        CoroutineScope(Dispatchers.IO).launch {
            val response: Response = client.newCall(request).execute()
            try {
                val mealModel: String? = response.body?.string()
                if (mealModel != null) {
                    updateList(mealModel)
                }
            } catch (_: Exception) {
            }
        }
    }

    private fun updateList(mealModel: String?) {
        CoroutineScope(Dispatchers.Main).launch {
            inflate.ingredientsLl.removeAllViews()
            val jsonObject = JSONObject(mealModel)
            val meals = JSONArray(jsonObject.get("meals").toString())
            val actualMeal = JSONObject(meals.get(0).toString())

            inflate.addFav.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    if (favouriteDAO.getisInFavourite(id) == 0) {
                        favouriteDAO.insertFavourite(
                            FavouriteData(
                                actualMeal.getString("idMeal").toLong(),
                                actualMeal.toString()
                            )
                        )
                    } else {
                        favouriteDAO.deleteFavourite(id)
                    }
                    updateFavourites()
                }
            }

            Glide.with(inflate.foodImg).load(actualMeal.getString("strMealThumb"))
                .into(inflate.foodImg)
            var key = 1
            actualMeal.keys().forEach {
                if (it.startsWith("strIngredient")) {
                    val replace = it.replace("strIngredient", "strMeasure")
                    val get = actualMeal.getString(it)
                    if (!(get.equals("null") || get.equals("") || get == null)) {
                        val textView = TextView(this@FoodDetailsActivity)
                        textView.text = ("$key) " + get + " " + actualMeal.get(replace) + "\n")
                        textView.setTextColor(Color.BLACK)
                        inflate.ingredientsLl.addView(textView)
                    }
                }
            }

            inflate.instructionsTv.text = actualMeal.getString("strInstructions")
        }
    }


}
