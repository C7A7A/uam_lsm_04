package com.example.expenser.data

data class Budget(val plannedBudget: Int? = null,
                  val moneySpent: Int? = null,
                  val dateEnd: String? = null,
                  val days: Int? = null,
                  val categories: MutableList<CategoryBudget>? = null
)