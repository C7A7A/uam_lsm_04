package com.example.expenser.ui.categories

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expenser.HelperUtils
import com.example.expenser.LoginActivity
import com.example.expenser.MainActivity
import com.example.expenser.R
import com.example.expenser.data.Category
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_categories.*
import kotlinx.android.synthetic.main.fragment_categories.view.*
import kotlinx.android.synthetic.main.single_category.view.*


class CategoriesFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_categories, container, false)
        (activity as MainActivity).fetchCategoriesFromDatabase(view)

        view.add_new_category_button.setOnClickListener {
            val newCategory = view.add_new_category.text.toString().trim()
            if (TextUtils.isEmpty(newCategory)) {
                Toast.makeText(context, "Category can't be empty", Toast.LENGTH_SHORT).show()
            } else {
//                Toast.makeText(context, newCategory, Toast.LENGTH_SHORT).show()
                (activity as MainActivity).insertCategoryToDatabase(newCategory)
                view.add_new_category.setText("")
            }
        }

        return view
    }
}