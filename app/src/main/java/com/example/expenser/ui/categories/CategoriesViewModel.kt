package com.example.expenser.ui.categories

import androidx.lifecycle.ViewModel
import com.example.expenser.data.Category
import com.google.firebase.database.*

class CategoriesViewModel : ViewModel() {

    fun basicCategoriesList(): MutableList<Category> {
         return mutableListOf(
             Category("Entertainment"),
             Category("Sport"),
             Category("Home"),
             Category("Bills"),
             Category("Health"),
             Category("Transport"),
             Category("Food"),
             Category("Clothes"),
             Category("Hobby"),
             Category("Other")
        )
    }

}