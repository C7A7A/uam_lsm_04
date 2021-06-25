package com.example.expenser.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expenser.MainActivity
import com.example.expenser.R
import com.example.expenser.data.CategoryBudget
import kotlinx.android.synthetic.main.single_budget_category.view.*

class HomeAdapter(private val categoriesList: MutableList<CategoryBudget>, val context: Context) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    private var mContext = context
    private var categories: MutableList<CategoryBudget> = categoriesList

    class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.single_budget_category_name
        val categoryBudget: TextView = itemView.single_budget_category_budget_planned
        val categoryBudgetSpent: TextView = itemView.single_budget_category_budget_money_spent
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.single_budget_category, parent,false)

        return HomeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val currentItem = categories[position]

        holder.categoryName.text = currentItem.name
        holder.categoryBudget.text = currentItem.plannedBudget
        holder.categoryBudgetSpent.text = currentItem.moneySpent
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}