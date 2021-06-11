package com.example.expenser.ui.analytics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.expenser.R

class AnalyticsFragment : Fragment() {

    private lateinit var analyticsViewModel: AnalyticsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        analyticsViewModel =
                ViewModelProvider(this).get(AnalyticsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_analytics, container, false)
        return root
    }
}