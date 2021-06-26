package com.example.expenser.ui.expenses

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expenser.MainActivity
import com.example.expenser.R
import com.example.expenser.data.Budget
import com.example.expenser.data.CategoryBudget
import kotlinx.android.synthetic.main.add_category_dialog.view.*
import kotlinx.android.synthetic.main.fragment_expenses.*
import kotlinx.android.synthetic.main.fragment_expenses.view.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class ExpensesFragment : Fragment() {

    lateinit var categoriesList: MutableList<CategoryBudget>
    lateinit var budget: Budget

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_expenses, container, false)

        categoriesList = mutableListOf()
        val expensesViewModel: ExpensesViewModel by viewModels()
        val expensesAdapter = context?.let { ExpensesAdapter(categoriesList, it) }

        // pick date
        view.expense_period_date.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH) + 1
            val day = c.get(Calendar.DAY_OF_MONTH)

            context?.let {
                DatePickerDialog(
                    it,
                    { _, year, monthOfYear, dayOfMonth ->

                        val dateText = "$dayOfMonth/$monthOfYear/$year"
                        expense_period_date.text = dateText

                    },
                    year,
                    month,
                    day
                )
            }?.show()
        }

        // add new category dialog
        view.add_new_category_expenses.setOnClickListener {

            val mDialogView = LayoutInflater.from(context).inflate(R.layout.add_category_dialog, null)
            val mBuilder = AlertDialog.Builder(context).setView(mDialogView)
            val mAlertDialog = mBuilder.show()

            (activity as MainActivity).fetchCategoriesFromDatabase(mDialogView, "spinner_expenses")

            mDialogView.add_category_expenses_add.setOnClickListener {

                val spinner = mDialogView.add_category_expenses_spinner
                val categoryName = spinner.selectedItem.toString()
                val categoryBudget = mDialogView.add_category_budget_expenses_category.text.toString()
                if (categoryName.isNotEmpty() and categoryBudget.isNotEmpty() and categoriesList.none{ it.name == categoryName }) {
                    categoriesList.add(CategoryBudget(categoryName, categoryBudget, "0"))

                    view.expenses_categories_list.adapter = expensesAdapter
                    view.expenses_categories_list.layoutManager = LinearLayoutManager(context)

                    planned_expenses.text = expensesViewModel.sumTotalBudget(categoriesList).toString()

                    mAlertDialog.dismiss()

                } else {
                    Toast.makeText(context, "You didn't set any budget or added this category already", Toast.LENGTH_LONG).show()
                }
            }

            mDialogView.add_category_expenses_cancel.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }

        // submit new budget
        view.submit_new_expenses.setOnClickListener {
//            Toast.makeText(context, "SUBMIT EXPENSES", Toast.LENGTH_SHORT).show()
            val plannedBudget = view.budget.text.toString()

            var dateEnd = expense_period_date.text.toString()
            val formatter = DateTimeFormatter.ofPattern("d/M/yyyy", Locale.ENGLISH)
            if (dateEnd.isNotEmpty()) {
                dateEnd = LocalDate.parse(dateEnd, formatter).toString()
            }

            val plannedExpenses = planned_expenses.text.toString()

            if (plannedBudget.isEmpty()) {
                Toast.makeText(context, "Set a budget", Toast.LENGTH_SHORT).show()
            } else if (dateEnd.isEmpty()) {
                Toast.makeText(context, "Pick a date", Toast.LENGTH_SHORT).show()
            } else {
//                val dateFormat = DateTimeFormatter.ofPattern("dd/mm/yyyy", Locale.ENGLISH)
//                val dateEnd = LocalDate.parse(dateEnd, dateFormat)
                if (LocalDate.parse(dateEnd).isBefore(LocalDate.now())) {
                    Toast.makeText(context, "Pick date in the future", Toast.LENGTH_SHORT).show()
                } else {
                    budget = Budget(plannedBudget, plannedExpenses, "0", dateEnd, categoriesList, true)

                    (activity as MainActivity).insertBudgetToDatabase(budget)

                    clearData(view)
                    expensesAdapter?.clearCategories(categoriesList)
                }
            }
        }
        return view
    }

    private fun clearData(view: View) {
        view.budget.setText("")
        view.expense_period_date.text = ""
        view.planned_expenses.text = ""
    }
}