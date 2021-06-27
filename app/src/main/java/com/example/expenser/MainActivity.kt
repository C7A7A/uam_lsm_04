package com.example.expenser

import android.app.AlertDialog
import android.content.ContentValues
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expenser.data.Budget
import com.example.expenser.data.Category
import com.example.expenser.data.CategoryBudget
import com.example.expenser.ui.analytics.AnalyticsViewModel
import com.example.expenser.ui.categories.CategoriesAdapter
import com.example.expenser.ui.home.HomeAdapter
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.add_category_dialog.view.*
import kotlinx.android.synthetic.main.budget_date_end_dialog.view.*
import kotlinx.android.synthetic.main.fragment_analytics.view.*
import kotlinx.android.synthetic.main.fragment_categories.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_single_budget.view.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var categoriesList: MutableList<Category>
    private lateinit var categoriesData: MutableMap<String, MutableList<Int>>
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

        if (newBudget.categories.isNullOrEmpty()) {
            newBudget.categories?.add(CategoryBudget("Any", newBudget.plannedBudget, "0"))
        }

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
                                    val moneySpentBudget = (budgetFromDB.moneySpent?.toInt()?.plus(categoryExpense.toInt())).toString()

                                    database.child(budget.key.toString()).child("categories").child(index.toString()).child("moneySpent").setValue(moneySpent)
                                    database.child(budget.key.toString()).child("moneySpent").setValue(moneySpentBudget)
                                }
                            }
                            refreshActivity()
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

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
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

                            // check if budget dateEnd has passed
                            val dateEnd = LocalDate.parse(budgetFromDB.dateEnd, DateTimeFormatter.ISO_DATE)
                            if (dateEnd.isBefore(LocalDate.now())) {
                                val dialogView = LayoutInflater.from(this@MainActivity).inflate(R.layout.budget_date_end_dialog, null)
                                val builder = AlertDialog.Builder(this@MainActivity).setView(dialogView)

                                builder.setTitle(R.string.budget_date_end_title)
                                val alertDialog = builder.show()

                                dialogView.budget_date_end_no.setOnClickListener {
                                    alertDialog.dismiss()
                                }

                                dialogView.budget_date_end_yes.setOnClickListener {
                                    database.child(budget.key.toString()).child("active").setValue(false)
                                    Toast.makeText(this@MainActivity, "Budget submitted successfully", Toast.LENGTH_SHORT).show()

                                    refreshActivity()

                                    alertDialog.dismiss()
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

    fun fetchDataAboutCategories(view: View) {
        val user = HelperUtils.getCurrentUser()
        if (user != null) {
            database = FirebaseDatabase.getInstance().getReference(user.uid).child("budgets")
        }

        categoriesData = mutableMapOf()

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    categoriesData.clear()
                    for (budget in dataSnapshot.children) {
                        val budgetFromDB = budget.getValue(Budget::class.java)

                        budgetFromDB?.categories?.forEach {
                            if (categoriesData.containsKey(it.name.toString())) {
                                categoriesData[it.name.toString()]!![0] += it.plannedBudget?.toInt()!!
                                categoriesData[it.name.toString()]!![1] += it.moneySpent?.toInt()!!
                            } else {
                                categoriesData[it.name.toString()] = mutableListOf(it.plannedBudget?.toInt()!!, it.moneySpent?.toInt()!!)
                            }
                        }
                    }
                    createPieChartAnalytics(view, categoriesData)
                    createBarChartAnalytics(view, categoriesData)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "deleteCategory:onCancelled", databaseError.toException())
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
                Log.w(ContentValues.TAG, "fetchDataAboutCategories:onCancelled", databaseError.toException())
            }
        })
    }

    // refresh activity
    private fun refreshActivity() {
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    private fun createPieChartAnalytics(view: View, categoriesData: MutableMap<String, MutableList<Int>>) {
        val analyticsViewModel: AnalyticsViewModel by viewModels()

        val pieChart: PieChart = view.pie_chart_analytics

        val moneySpent: ArrayList<PieEntry> = arrayListOf()
        categoriesData.forEach { category ->
            if (category.value[1] > 0) {
                moneySpent.add(PieEntry(category.value[1].toFloat(), category.key))
            }
        }

        val pieDataSet = PieDataSet(moneySpent, "")
        pieDataSet.colors = analyticsViewModel.colorfulColors()
        pieDataSet.valueTextColor = Color.BLACK
        pieDataSet.valueTextSize = 16f

        val pieData = PieData(pieDataSet)

        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.centerText = "Money spent"
        pieChart.animate()

        pieChart.notifyDataSetChanged()
        pieChart.invalidate()
    }

    private fun createBarChartAnalytics(view: View, categoriesData: MutableMap<String, MutableList<Int>>) {
        val analyticsViewModel: AnalyticsViewModel by viewModels()
        val barChart: BarChart = view.bar_chart_analytics

        barChart.setDrawValueAboveBar(true)
        barChart.setDrawGridBackground(true)
        barChart.description.isEnabled = false

        val plannedBudgets: ArrayList<BarEntry> = arrayListOf()
        val moneySpent: ArrayList<BarEntry> = arrayListOf()
        val xAxisLabels: ArrayList<String> = arrayListOf()

        var counter = 1f
        categoriesData.forEach { category ->
            plannedBudgets.add(BarEntry(counter, category.value[0].toFloat()))
            moneySpent.add(BarEntry(counter, category.value[1].toFloat()))

            xAxisLabels.add(category.key)

            counter++
        }

        val barDataSet = BarDataSet(plannedBudgets, "Planned budget")
        barDataSet.colors = analyticsViewModel.colorfulColors()

        val barDataSet2 = BarDataSet(moneySpent, "Money spent")
        barDataSet2.colors = analyticsViewModel.darkerColorfulColors()

        val barData = BarData(barDataSet, barDataSet2)
        barData.barWidth = 0.38f

        barChart.data = barData
        barChart.setVisibleXRangeMaximum(5f)
        barChart.groupBars(0f, 0.2f, 0.02f)

        val xAxis = barChart.xAxis

        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels)

        xAxis.setCenterAxisLabels(true)
        xAxis.position = XAxis.XAxisPosition.TOP
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.textSize = 10f
        xAxis.axisLineColor = Color.WHITE
        xAxis.axisMinimum = 0f

        barChart.notifyDataSetChanged()
        barChart.invalidate()
    }
}