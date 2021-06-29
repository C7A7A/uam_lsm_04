package com.example.expenser.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.expenser.MainActivity
import com.example.expenser.R
import kotlinx.android.synthetic.main.budget_date_end_dialog.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeView: View = inflater.inflate(R.layout.fragment_home, container, false)

        (activity as MainActivity).fetchFirstActiveBudget(homeView)

        homeView.add_expense_button.setOnClickListener {
            if (homeView.insert_expense.text.isNotEmpty() && homeView.choose_category_spinner.count > 0) {
                val expense = homeView.insert_expense.text
                val category = homeView.choose_category_spinner.selectedItem
                (activity as MainActivity).updateActiveBudget(category.toString(), expense.toString())
            }
        }

        homeView.end_budget_period.setOnClickListener {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.budget_date_end_dialog, null)
            val builder = AlertDialog.Builder(context).setView(dialogView)

            builder.setTitle(R.string.budget_date_end_title_manualy)
            val alertDialog = builder.show()

            dialogView.budget_date_end_no.setOnClickListener {
                alertDialog.dismiss()
            }

            dialogView.budget_date_end_yes.setOnClickListener {
                (activity as MainActivity).submitFirstActiveBudget()

                alertDialog.dismiss()
            }
        }

        return homeView
    }

}