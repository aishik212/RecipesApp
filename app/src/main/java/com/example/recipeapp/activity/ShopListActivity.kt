package com.example.recipeapp.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.recipeapp.adapter.ShoppingListAdapter
import com.example.recipeapp.databinding.ShoppingListLayoutBinding
import com.example.recipeapp.model.db.ShoppingItemsData
import com.example.recipeapp.model.db.WeeklyShoppingItemsData
import com.example.recipeapp.roomdb.dao.ShoppingListDAO
import com.example.recipeapp.roomdb.dao.WeeklyShoppingListDAO
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ShopListActivity : AppCompatActivity() {


    @Inject
    lateinit var shoppingListDAO: ShoppingListDAO

    @Inject
    lateinit var weeklyShoppingListDAO: WeeklyShoppingListDAO

    lateinit var homePageFoodAdapter: ShoppingListAdapter
    val mealList: MutableList<ShoppingItemsData> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflate = ShoppingListLayoutBinding.inflate(layoutInflater)
        setContentView(inflate.root)
        val recyclerView = inflate.recyclerView
        homePageFoodAdapter =
            ShoppingListAdapter(mealList, this@ShopListActivity, ::onClickListener)
        recyclerView.adapter = homePageFoodAdapter
        CoroutineScope(Dispatchers.IO).launch {
            updateList()
        }

    }

    private fun updateList() {
        mealList.clear()
        shoppingListDAO.getAllShoppingList().iterator().forEach {
            mealList.add(it)
        }

        val allWeeklyShoppingList = weeklyShoppingListDAO.getAllWeeklyShoppingList()
        val MondayFoods: List<WeeklyShoppingItemsData> =
            allWeeklyShoppingList.filter { it.day == "Monday" }
        val TuesdayFoods = allWeeklyShoppingList.filter { it.day == "Tuesday" }
        val WednesdayFoods = allWeeklyShoppingList.filter { it.day == "Wednesday" }
        val ThursdayFoods = allWeeklyShoppingList.filter { it.day == "Thursday" }
        val FridayFoods = allWeeklyShoppingList.filter { it.day == "Friday" }
        val SaturdayFoods = allWeeklyShoppingList.filter { it.day == "Saturday" }
        val SundayFoods = allWeeklyShoppingList.filter { it.day == "Sunday" }

        val sortedBy = mutableListOf<List<WeeklyShoppingItemsData>>()
        sortedBy.add(MondayFoods)
        sortedBy.add(TuesdayFoods)
        sortedBy.add(WednesdayFoods)
        sortedBy.add(ThursdayFoods)
        sortedBy.add(FridayFoods)
        sortedBy.add(SaturdayFoods)
        sortedBy.add(SundayFoods)
        sortedBy.iterator().forEach {
            it.iterator().forEach {
                val s = it.day + "\n" + it.items
                Log.d("texts", "updateList: " + it.day)
                Log.d("texts", "updateList: " + it.items?.replace(it.day.toString(), ""))
                mealList.add(ShoppingItemsData(it.id, s))
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            homePageFoodAdapter.notifyDataSetChanged()
        }
    }

    private fun onClickListener(l: Long, view: View?) {
        CoroutineScope(Dispatchers.IO).launch {
            shoppingListDAO.delete(l)
            updateList()
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(applicationContext, "Deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
