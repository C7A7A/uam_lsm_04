package com.example.expenser.ui.categories

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.expenser.HelperUtils
import com.example.expenser.MainActivity
import com.example.expenser.R
import com.example.expenser.data.Category
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.single_category.view.*

class CategoriesAdapter(private val categoriesList: MutableList<Category>, val context: Context) : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    private var mContext = context
    private var categories: MutableList<Category> = categoriesList

    inner class CategoriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.single_category

        fun bind(category: Category, index: Int) {
            val categoryName = itemView.findViewById<TextView>(R.id.single_category)
            val deleteButton = itemView.findViewById<ImageButton>(R.id.category_button_delete)

            categoryName.text = category.name

            deleteButton.setOnClickListener{ deleteItem(index) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.single_category, parent,false)

        return CategoriesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val currentItem = categoriesList[position]

        holder.textView.text = currentItem.name
        holder.bind(categories[position], position)
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    fun deleteItem(index: Int) {
        if (mContext is MainActivity) {
            (mContext as MainActivity).deleteCategoryFromDatabase(categories[index])
            categories.removeAt(index)
            notifyDataSetChanged()
        }
    }
}