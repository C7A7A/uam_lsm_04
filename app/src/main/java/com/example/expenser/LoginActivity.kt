package com.example.expenser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.example.expenser.ui.categories.CategoriesViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loginUser(email: String, password: String) {
        auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(baseContext, task.exception.toString(), Toast.LENGTH_LONG).show()
                }
            }
    }

    fun registerUser(email: String, password: String) {
        auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    insertBasicCategoriesToDatabase()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(baseContext, task.exception.toString(), Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun insertBasicCategoriesToDatabase() {
        database = Firebase.database.reference

        val user = HelperUtils.getCurrentUser()

        val categoriesViewModel: CategoriesViewModel by viewModels()
        val categoriesList = categoriesViewModel.basicCategoriesList()

        categoriesList.forEach {
            val id = database.push().key
            database.child(user?.uid.toString()).child("categories").child(id.toString())
                .setValue(it)
        }
    }
}