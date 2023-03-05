package com.example.recipeapp.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.recipeapp.R
import com.example.recipeapp.model.ShoppingItemsData

class ShoppingListAdapter(
    private val meals: List<ShoppingItemsData>,
    private val activity: Activity,
    private val onClickListener: (id: Long, view: View?) -> Unit
) :
    Adapter<ShoppingListAdapter.ViewHolder>() {

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.foodname)
        val deleteBtn: Button = itemView.findViewById(R.id.delete_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val from = LayoutInflater.from(activity)
        val inflate = from.inflate(R.layout.shopping_list_row, parent, false)
        return ViewHolder(inflate.rootView)
    }

    override fun getItemCount(): Int = meals.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val s = meals[position]
/*
        Glide.with(holder.imageView).load(strMealThumb)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(holder.imageView)
*/
        holder.textView.text = s.items
        holder.deleteBtn.setOnClickListener {
            s.id?.let { it1 -> onClickListener(it1, it) }
        }
/*
        holder.itemView.setOnClickListener {
            val intent = Intent(activity, FoodDetailsActivity::class.java)
            intent.putExtra("id", idMeal)
            activity.startActivity(intent)
        }
*/
    }
}