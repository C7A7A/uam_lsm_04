package com.example.expenser.ui.categories

import androidx.lifecycle.ViewModel
import com.example.expenser.data.Category

class CategoriesViewModel : ViewModel() {

    private lateinit var categories: MutableList<String>

    fun basicCategoriesList(): List<Category> {
         return listOf(
             Category("Entertainment"),
             Category("Sport"),
             Category("Home"),
             Category( "Bills"),
             Category("Health"),
             Category("Transport"),
             Category("Food"),
             Category("Clothes"),
             Category("VOD"),
             Category("Hobby"),
             Category("Car"),
             Category("Other")
        )
    }
}