package com.example.recipeapp.activity

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.recipeapp.R
import com.example.recipeapp.databinding.FoodDetailsLayoutBinding
import com.example.recipeapp.databinding.SelectDayDialogBinding
import com.example.recipeapp.model.db.FavouriteData
import com.example.recipeapp.model.db.ShoppingItemsData
import com.example.recipeapp.model.db.WeeklyShoppingItemsData
import com.example.recipeapp.roomdb.dao.FavouriteDAO
import com.example.recipeapp.roomdb.dao.ShoppingListDAO
import com.example.recipeapp.roomdb.dao.WeeklyShoppingListDAO
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

    @Inject
    lateinit var shoppingListDAO: ShoppingListDAO

    @Inject
    lateinit var weeklyShoppingListDAO: WeeklyShoppingListDAO

    var id: String? = null

    var ingredientsList: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflate = FoodDetailsLayoutBinding.inflate(layoutInflater)
        setContentView(inflate.root)
        id = intent.extras?.getString("id", null)
        inflate.addFav.visibility = GONE
        inflate.addShopping.visibility = GONE
        inflate.addCalender.visibility = GONE
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
                if (favouriteDAO.exists(id) == 0) {
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
                    if (favouriteDAO.exists(id) == 0) {
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(applicationContext, "Added", Toast.LENGTH_SHORT).show()
                        }
                        favouriteDAO.insert(
                            FavouriteData(
                                actualMeal.getString("idMeal").toLong(),
                                actualMeal.toString()
                            )
                        )
                    } else {
                        favouriteDAO.delete(id)
                    }
                    updateFavourites()
                }
            }
            inflate.addShopping.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    if (shoppingListDAO.exists(id) == 0) {
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(applicationContext, "Added", Toast.LENGTH_SHORT).show()
                        }
                        shoppingListDAO.insert(
                            ShoppingItemsData(
                                actualMeal.getString("idMeal").toLong(),
                                ingredientsList
                            )
                        )
                    } else {
                        id?.let {
                            shoppingListDAO.delete(it.toLong())
                        }
                    }
                    updateFavourites()
                }
            }

            inflate.addCalender.setOnClickListener {
                val alertDialog = AlertDialog.Builder(this@FoodDetailsActivity)
                val inflate1 = SelectDayDialogBinding.inflate(layoutInflater)
                alertDialog.setView(inflate1.root)
                var create: AlertDialog? = null
                inflate1.button.setOnClickListener {
                    id?.let {
                        CoroutineScope(Dispatchers.IO).launch {
                            weeklyShoppingListDAO.insert(
                                WeeklyShoppingItemsData(
                                    ingredientsList,
                                    inflate1.spinner.selectedItem.toString()
                                )
                            )
                        }
                    }
                    create?.dismiss()
                }
                create = alertDialog.create()
                create.show()
            }

            Glide.with(inflate.foodImg).load(actualMeal.getString("strMealThumb"))
                .diskCacheStrategy(
                    DiskCacheStrategy.AUTOMATIC
                )
                .into(inflate.foodImg)
            var key = 1
            ingredientsList = actualMeal.get("strMeal").toString() + "\n"
            actualMeal.keys().forEach {
                if (it.startsWith("strIngredient")) {
                    val replace = it.replace("strIngredient", "strMeasure")
                    val get = actualMeal.getString(it)
                    if (!(get.equals("null") || get.equals("") || get == null)) {
                        val textView = TextView(this@FoodDetailsActivity)
                        textView.text = ("$key) " + get + " " + actualMeal.get(replace) + "\n")
                        ingredientsList += "$key) " + get + " " + actualMeal.get(replace) + "\n"
                        key++
                        textView.setTextColor(Color.BLACK)
                        inflate.ingredientsLl.addView(textView)
                    }
                }
            }

            inflate.instructionsTv.text = actualMeal.getString("strInstructions")
            inflate.addFav.visibility = VISIBLE
            inflate.addShopping.visibility = VISIBLE
            inflate.addCalender.visibility = VISIBLE
        }
    }


}
