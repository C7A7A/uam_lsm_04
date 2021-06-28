package com.example.expenser.ui.analytics

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expenser.R
import com.example.expenser.data.Budget
import com.example.expenser.ui.home.HomeAdapter
import kotlinx.android.synthetic.main.fragment_analytics.view.*
import kotlinx.android.synthetic.main.single_budget_overview.view.*

class AnalyticsBudgetsAdapter(private val budgetsList: MutableList<Budget>, val context: Context) : RecyclerView.Adapter<AnalyticsBudgetsAdapter.AnalyticsBudgetsViewHolder>() {

    private var budgets: MutableList<Budget> = budgetsList

    class AnalyticsBudgetsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val budgetDateEnd: TextView = itemView.single_budget_overview_date
        val budgetActive: ImageButton = itemView.single_budget_overview_active
        val budgetBudget: TextView = itemView.single_budget_overview_budget
        val budgetExpenses: TextView = itemView.single_budget_overview_expenses
        val budgetCategoriesAdapter: RecyclerView = itemView.single_budget_overview_categories
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnalyticsBudgetsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.single_budget_overview, parent,false)

        return AnalyticsBudgetsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AnalyticsBudgetsViewHolder, position: Int) {
        val currentItem = budgets[position]

        holder.budgetDateEnd.text = currentItem.dateEnd
        if (currentItem.active.toString() == "true") {
            holder.budgetActive.setBackgroundResource(R.drawable.ic_check_green_24dp)
        } else {
            holder.budgetActive.setBackgroundResource(R.drawable.ic_cross_red_24dp)
        }

        holder.budgetBudget.text = currentItem.plannedBudget
        holder.budgetExpenses.text = currentItem.moneySpent

        holder.budgetCategoriesAdapter.adapter = HomeAdapter(currentItem.categories!!, context)
        holder.budgetCategoriesAdapter.layoutManager = LinearLayoutManager(context)
    }

    override fun getItemCount(): Int {
        return budgets.size
    }
}