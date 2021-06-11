package com.example.expenser.ui.expenses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.expenser.R

class ExpensesFragment : Fragment() {

    private lateinit var expensesViewModel: ExpensesViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        expensesViewModel =
                ViewModelProvider(this).get(ExpensesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_expenses, container, false)
        return root
    }
}