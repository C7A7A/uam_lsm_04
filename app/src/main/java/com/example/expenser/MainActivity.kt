package com.example.expenser

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.solver.widgets.Helper
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expenser.data.Budget
import com.example.expenser.data.Category
import com.example.expenser.data.CategoryBudget
import com.example.expenser.ui.categories.CategoriesAdapter
import com.example.expenser.ui.home.HomeAdapter
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.add_category_dialog.view.*
import kotlinx.android.synthetic.main.fragment_categories.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_single_budget.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var categoriesList: MutableList<Category>
    lateinit var spinner: Spinner


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

    fun insertCategoryToDatabase(newCategory: String) {
        database = Firebase.database.reference

        val id = database.push().key
        val category = Category(newCategory)
        val user = HelperUtils.getCurrentUser()

        database.child(user?.uid.toString()).child("categories").child(id.toString()).setValue(category)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "Category added successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(baseContext, task.exception.toString(), Toast.LENGTH_LONG).show()
                }
            }
    }

    fun insertBudgetToDatabase(newBudget: Budget) {
        database = Firebase.database.reference

        val id = database.push().key
        val user = HelperUtils.getCurrentUser()

        database.child(user?.uid.toString()).child("budgets").child(id.toString()).setValue(newBudget)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "Budget was added successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(baseContext, task.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun updateActiveBudget(view: View, categoryName: String, categoryExpense: String) {
        database = Firebase.database.reference

        val user = HelperUtils.getCurrentUser()
        if (user != null) {
            database = FirebaseDatabase.getInstance().getReference(user.uid).child("budgets")
        }

        categoriesList = mutableListOf()

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    categoriesList.clear()
                    for (budget in dataSnapshot.children) {
                        val budgetFromDB = budget.getValue(Budget::class.java)
                        if (budgetFromDB?.active == true) {
                            budgetFromDB.categories?.forEachIndexed { index, it ->
                                if (it.name == categoryName) {
                                    val moneySpent = (it.moneySpent?.toInt()?.plus(categoryExpense.toInt())).toString()
                                    val moneySpentBudget = (budgetFromDB.moneySpent?.toInt()?.plus(moneySpent.toInt())).toString()

                                    database.child(budget.key.toString()).child("categories").child(index.toString()).child("moneySpent").setValue(moneySpent)
                                    database.child(budget.key.toString()).child("moneySpent").setValue(moneySpentBudget)
                                }
                            }
                            return
                        }
                    }

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "fetchFirstActiveBudget:onCancelled", databaseError.toException())
            }
        })

    }

    fun fetchCategoriesFromDatabase(view: View, purpose: String) {
        val user = HelperUtils.getCurrentUser()
        if (user != null) {
            database = FirebaseDatabase.getInstance().getReference(user.uid).child("categories")
        }

        categoriesList = mutableListOf()

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    categoriesList.clear()
                    for (category in dataSnapshot.children) {
                        val categoryFromDB = category.getValue(Category::class.java)
                        categoriesList.add(categoryFromDB!!)
                    }

                    when (purpose) {
                        "category_list" -> {
                            view.categories_list.adapter = CategoriesAdapter(categoriesList, this@MainActivity)
                            view.categories_list.layoutManager = LinearLayoutManager(baseContext)
                        }
                        "spinner_expenses" -> {
                            spinner = view.add_category_expenses_spinner
                            val adapter = ArrayAdapter(this@MainActivity, R.layout.spinner_item, categoriesList.map { it.name })
                            adapter.setDropDownViewResource(R.layout.spinner_item)
                            spinner.adapter = adapter
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "fetchCategories:onCancelled", databaseError.toException())
            }
        })
    }

    fun fetchFirstActiveBudget(homeView: View) {
        val user = HelperUtils.getCurrentUser()
        if (user != null) {
            database = FirebaseDatabase.getInstance().getReference(user.uid).child("budgets")
        }

        val categoriesList: MutableList<CategoryBudget> = mutableListOf()

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    categoriesList.clear()
                    for (budget in dataSnapshot.children) {
                        val budgetFromDB = budget.getValue(Budget::class.java)
                        if (budgetFromDB?.active == true) {
                            budgetFromDB.categories?.forEach {
                                val categoryBudget = CategoryBudget(it.name, it.plannedBudget, it.moneySpent)
                                categoriesList.add(categoryBudget)
                            }

                            // set categories from budget to spinner
                            spinner = homeView.choose_category_spinner
                            val adapter = ArrayAdapter(this@MainActivity, R.layout.spinner_item, categoriesList.map { it.name })
                            adapter.setDropDownViewResource(R.layout.spinner_item)
                            spinner.adapter = adapter

                            // set data to single budget
                            homeView.single_budget_budget.text = budgetFromDB.plannedBudget
                            homeView.single_budget_planned_expenses.text = budgetFromDB.plannedExpenses
                            homeView.single_budget_expenses.text = budgetFromDB.moneySpent
                            homeView.single_budget_date.text = budgetFromDB.dateEnd

                            // set categories to single budget recycler view
                            homeView.categories_single_budget.adapter = HomeAdapter(categoriesList, this@MainActivity)
                            homeView.categories_single_budget.layoutManager = LinearLayoutManager(baseContext)

                            return
                        }
                    }

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "fetchFirstActiveBudget:onCancelled", databaseError.toException())
            }
        })
    }


    fun deleteCategoryFromDatabase(categoryToDelete: Category) {
        database = Firebase.database.reference

        val user = HelperUtils.getCurrentUser()

        val categoryQuery = database.child(user?.uid.toString()).child("categories").orderByChild("name").equalTo(categoryToDelete.name)

        categoryQuery.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (category in dataSnapshot.children) {
                        category.ref.removeValue()
                        Toast.makeText(baseContext, "Category deleted successfully", Toast.LENGTH_SHORT).show()
                        return
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "deleteCategory:onCancelled", databaseError.toException())
            }
        })
    }
}