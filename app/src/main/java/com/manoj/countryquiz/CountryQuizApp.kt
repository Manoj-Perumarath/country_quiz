package com.manoj.countryquiz

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CountryQuizApp : Application() {

    override fun onCreate() {
        super.onCreate()

    }
}