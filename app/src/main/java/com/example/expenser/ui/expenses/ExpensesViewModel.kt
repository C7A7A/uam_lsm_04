package com.example.expenser.ui.expenses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenser.data.CategoryBudget

class ExpensesViewModel : ViewModel() {
    fun sumTotalBudget(categoriesList: MutableList<CategoryBudget>): Int {
        var totalBudget = 0
        categoriesList.forEach {
            totalBudget += it.plannedBudget?.toInt() ?: 0
        }
        return totalBudget
    }
}