package com.manoj.countryquiz.adapters

import android.widget.RadioGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("countryName")
fun setCountryName(view: TextView, title: String?) {
    view.text = title
}

@BindingAdapter("onCheckedChanged")
fun bindRadioGroupListener(radioGroup: RadioGroup, listener: RadioGroup.OnCheckedChangeListener?) {
    radioGroup.setOnCheckedChangeListener(listener)
}