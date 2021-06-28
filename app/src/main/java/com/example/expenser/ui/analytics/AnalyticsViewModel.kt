package com.example.expenser.ui.analytics

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AnalyticsViewModel : ViewModel() {
    fun colorfulColors(): List<Int> {
        return listOf(
            Color.rgb(193, 37, 82),
            Color.rgb(230, 19, 0),
            Color.rgb(255, 102, 0),
            Color.rgb(230, 80, 0),
            Color.rgb(245, 199, 0),
            Color.rgb(255, 234, 16),
            Color.rgb(60, 191, 23),
            Color.rgb(106, 150, 31),
            Color.rgb(20, 191, 103),
            Color.rgb(179, 100, 53),
            Color.rgb(25, 112, 128),
            Color.rgb(26, 16, 128),
            Color.rgb(20, 0, 0),
            Color.rgb(235, 59, 222),
            Color.rgb(235, 82, 108)
        )
    }

    fun darkerColorfulColors(): List<Int> {
        return listOf(
            Color.rgb(173, 37, 82),
            Color.rgb(210, 19, 0),
            Color.rgb(235, 102, 0),
            Color.rgb(210, 80, 0),
            Color.rgb(225, 199, 0),
            Color.rgb(235, 234, 16),
            Color.rgb(40, 191, 23),
            Color.rgb(86, 150, 31),
            Color.rgb(0, 191, 103),
            Color.rgb(159, 100, 53),
            Color.rgb(5, 112, 128),
            Color.rgb(6, 16, 128),
            Color.rgb(0, 0, 0),
            Color.rgb(215, 59, 222),
            Color.rgb(215, 82, 108)
        )
    }
}