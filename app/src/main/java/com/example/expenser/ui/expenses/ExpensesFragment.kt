package com.example.expenser.ui.expenses

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expenser.MainActivity
import com.example.expenser.R
import com.example.expenser.data.Category
import com.example.expenser.data.CategoryBudget
import com.example.expenser.ui.categories.CategoriesAdapter
import kotlinx.android.synthetic.main.add_category_dialog.view.*
import kotlinx.android.synthetic.main.fragment_categories.view.*
import kotlinx.android.synthetic.main.fragment_expenses.*
import kotlinx.android.synthetic.main.fragment_expenses.view.*
import java.util.*

class ExpensesFragment : Fragment() {

    lateinit var categoriesList: MutableList<CategoryBudget>

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_expenses, container, false)

        categoriesList = mutableListOf()

        view.expense_period_date.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
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

        view.add_new_category_expenses.setOnClickListener {
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.add_category_dialog, null)
            val mBuilder = AlertDialog.Builder(context).setView(mDialogView).setTitle("Add new category to expenses")
            val mAlertDialog = mBuilder.show()

            (activity as MainActivity).fetchCategoriesFromDatabase(mDialogView, "spinner_expenses")

            mDialogView.add_category_expenses_add.setOnClickListener {

                val spinner = mDialogView.add_category_expenses_spinner
                val categoryName = spinner.selectedItem.toString()
                val categoryBudget = mDialogView.add_category_budget_expenses_category.text.toString()
                if (categoryName.isNotEmpty() and categoryBudget.isNotEmpty()) {
                    categoriesList.add(CategoryBudget(categoryName, categoryBudget, 0))

                    view.categories_list.adapter =
                        context?.let { it1 -> ExpensesAdapter(categoriesList, it1) }
                    view.categories_list.layoutManager = LinearLayoutManager(context)

                    mAlertDialog.dismiss()

                } else {
                    Toast.makeText(context, "Choose category name and budget", Toast.LENGTH_SHORT).show()
                }
            }

            mDialogView.add_category_expenses_cancel.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }

        view.submit_new_expenses.setOnClickListener {
            Toast.makeText(context, "SUBMIT EXPENSES", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}