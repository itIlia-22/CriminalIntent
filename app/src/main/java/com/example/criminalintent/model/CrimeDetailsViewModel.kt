package com.example.criminalintent.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.criminalintent.repository.CrimeRepository
import java.io.File
import java.util.*

class CrimeDetailsViewModel() : ViewModel() {
    private val crimeRepository =
        CrimeRepository.get()
    private val crimeIdLiveData =
        MutableLiveData<UUID>()
    var crimeLiveData: LiveData<Crime?> =
        Transformations.switchMap(crimeIdLiveData) { crimeId ->
            crimeRepository.getCrime(crimeId)
        }
    fun loadCrime(crimeId: UUID) {
        crimeIdLiveData.value = crimeId
    }
    fun getPhotoFile(crime: Crime): File {
        return crimeRepository.getPhotoFile(crime)
    }


    fun saveCrime(crime: Crime) {
        crimeRepository.updateCrime(crime)
    }


}