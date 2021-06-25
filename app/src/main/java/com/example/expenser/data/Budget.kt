package com.example.expenser.data

data class Budget(val plannedBudget: String? = null,
                  val plannedExpenses: String? = null,
                  val moneySpent: String? = null,
                  val dateEnd: String? = null,
                  val categories: MutableList<CategoryBudget>? = null,
                  val active: Boolean? = null
)