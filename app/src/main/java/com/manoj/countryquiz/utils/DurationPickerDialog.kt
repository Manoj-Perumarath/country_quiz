package com.manoj.countryquiz.utils

import android.content.Context
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog

fun Context.showSecondsPicker(onTimeSelected: (minutes: String, seconds: String) -> Unit) {
    val minutePicker = NumberPicker(this).apply {
        minValue = 0
        maxValue = 59
        value = 0
    }

    val secondPicker = NumberPicker(this).apply {
        minValue = 0
        maxValue = 59
        value = 0
    }

    val pickerLayout = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        setPadding(50, 20, 50, 20)
        gravity = Gravity.CENTER
        addView(minutePicker)
        addView(secondPicker)
    }

    AlertDialog.Builder(this)
        .setTitle("Pick Minutes and Seconds")
        .setView(pickerLayout)
        .setPositiveButton("OK") { _, _ ->
            val selectedMinutes = minutePicker.value
            val selectedSeconds = secondPicker.value
            onTimeSelected(String.format("%02d",selectedMinutes), String.format("%02d",selectedSeconds))

        }
        .setNegativeButton("Cancel", null)
        .show()
}
