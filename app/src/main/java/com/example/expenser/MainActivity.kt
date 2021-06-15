package com.example.expenser

import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.expenser.data.Category
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_expenses, R.id.navigation_categories, R.id.navigation_analytics))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun getCurrentUser(): FirebaseUser? {
        return Firebase.auth.currentUser
    }

    fun insertCategoryToDatabase(newCategory: String) {
        database = Firebase.database.reference

        val id = database.push().key
        val category = Category(newCategory)
        val user = getCurrentUser()

        database.child(user?.uid.toString()).child("categories").child(id.toString()).setValue(category)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "Category added successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(baseContext, task.exception.toString(), Toast.LENGTH_LONG).show()
                }
            }
    }
}