package com.example.recipeapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.activity.FoodDetailsActivity
import com.example.recipeapp.model.Meal

class HomePageFoodAdapter(private val meals: List<Meal>, private val activity: Activity) :
    Adapter<HomePageFoodAdapter.ViewHolder>() {

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.foodimg)
        val textView: TextView = itemView.findViewById(R.id.foodname)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val from = LayoutInflater.from(activity)
        val inflate = from.inflate(R.layout.recipes_list_rows, parent, false)
        return ViewHolder(inflate.rootView)
    }

    override fun getItemCount(): Int = meals.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (idMeal, strMeal, strMealThumb) = meals[position]
        Glide.with(holder.imageView).load(strMealThumb).into(holder.imageView)
        holder.textView.text = strMeal
        holder.itemView.setOnClickListener {
            val intent = Intent(activity, FoodDetailsActivity::class.java)
            intent.putExtra("id", idMeal)
            activity.startActivity(intent)
        }
    }
}