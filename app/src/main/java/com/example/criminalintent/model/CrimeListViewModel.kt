package com.example.criminalintent.model

import androidx.lifecycle.ViewModel
import com.example.criminalintent.repository.CrimeRepository
import java.io.File

class CrimeListViewModel : ViewModel() {

    val crimes = emptyList<Crime>()


    private val crimeRepository = CrimeRepository.get()
    val crimeListLiveData = crimeRepository.getCrimes()

    fun addCrime(crime: Crime) {
        crimeRepository.addCrime(crime)
    }


}