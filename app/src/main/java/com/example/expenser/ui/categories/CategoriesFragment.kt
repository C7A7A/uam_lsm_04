package com.example.expenser.ui.categories

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.expenser.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_categories.view.*


class CategoriesFragment : Fragment() {

    private lateinit var categoriesViewModel: CategoriesViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        categoriesViewModel = ViewModelProvider(this).get(CategoriesViewModel::class.java)

        val view: View = inflater.inflate(R.layout.fragment_categories, container, false)

        view.add_new_category_button.setOnClickListener {
            val newCategory = view.add_new_category.text.toString().trim()
            if (TextUtils.isEmpty(newCategory)) {
                Toast.makeText(context, "Category can't be empty", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, newCategory, Toast.LENGTH_SHORT).show()
//                insertDataToDatabase(newCategory)
            }
        }

        return view
    }

    private fun insertDataToDatabase(category: String) {
        val database = FirebaseDatabase.getInstance().getReference()
        val userId = database.push().key
//        database.child()
    }
}