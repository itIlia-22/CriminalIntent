package com.example.criminalintent.app

import android.app.Application
import com.example.criminalintent.repository.CrimeRepository

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        CrimeRepository.initialize(this)
    }
}