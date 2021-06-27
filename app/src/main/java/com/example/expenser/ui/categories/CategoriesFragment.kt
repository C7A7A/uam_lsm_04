package com.example.expenser.ui.categories

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.expenser.MainActivity
import com.example.expenser.R
import kotlinx.android.synthetic.main.fragment_categories.view.*

class CategoriesFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_categories, container, false)

        (activity as MainActivity).fetchCategoriesFromDatabase(view, "category_list")

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