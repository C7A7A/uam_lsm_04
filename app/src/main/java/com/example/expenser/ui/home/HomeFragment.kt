package com.example.expenser.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.expenser.MainActivity
import com.example.expenser.R
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
            val expense = homeView.insert_expense.text.toString()
            val category = homeView.choose_category_spinner.selectedItem.toString()

            (activity as MainActivity).updateActiveBudget(homeView, category, expense)
        }

        return homeView
    }
}