package com.example.expenser.ui.expenses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expenser.R
import com.example.expenser.data.Category
import com.example.expenser.data.CategoryBudget
import kotlinx.android.synthetic.main.single_category.view.*

class ExpensesAdapter(private val categoriesList: MutableList<CategoryBudget>, val context: Context) : RecyclerView.Adapter<ExpensesAdapter.ExpensesViewHolder>() {

    private var mContext = context
    private var categories: MutableList<CategoryBudget> = categoriesList

    inner class ExpensesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.single_category
        val categoryBudget: TextView = itemView.single_category_budget

        fun bind(category: CategoryBudget, index: Int) {
            val categoryName = itemView.findViewById<TextView>(R.id.single_category)
            val categoryBudget = itemView.findViewById<TextView>(R.id.single_category_budget)
            val deleteButton = itemView.findViewById<ImageButton>(R.id.category_button_delete)

            categoryName.text = category.name

            deleteButton.setOnClickListener{ deleteItem(index) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpensesAdapter.ExpensesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.single_category, parent,false)

        return ExpensesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExpensesAdapter.ExpensesViewHolder, position: Int) {
        val currentItem = categoriesList[position]

        holder.categoryName.text = currentItem.name
        holder.categoryBudget.text = currentItem.plannedBudget
        holder.bind(categories[position], position)
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    fun deleteItem(index: Int) {
        categories.removeAt(index)
        notifyDataSetChanged()
    }
}