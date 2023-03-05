package com.example.recipeapp.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.recipeapp.adapter.ShoppingListAdapter
import com.example.recipeapp.databinding.FavouriteFoodLayoutBinding
import com.example.recipeapp.model.ShoppingItemsData
import com.example.recipeapp.roomdb.dao.ShoppingListDAO
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ShopListActivity : AppCompatActivity() {


    @Inject
    lateinit var shoppingListDAO: ShoppingListDAO
    lateinit var homePageFoodAdapter: ShoppingListAdapter
    val mealList: MutableList<ShoppingItemsData> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflate = FavouriteFoodLayoutBinding.inflate(layoutInflater)
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
            Log.d("texts", "onCreate: " + it)
            mealList.add(it)
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
