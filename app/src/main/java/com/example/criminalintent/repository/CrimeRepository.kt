package com.example.criminalintent.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.criminalintent.database.CrimeDataBase
import com.example.criminalintent.database.migration_1_2
import com.example.criminalintent.model.Crime
import java.io.File
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "crime-database"

class CrimeRepository private constructor(context: Context) {
    private val filesDir = context.applicationContext.filesDir
    private val database: CrimeDataBase =
        Room.databaseBuilder(
            context.applicationContext,
            CrimeDataBase::class.java,
            DATABASE_NAME
        ).addMigrations(migration_1_2 )
            .build()
    private val crimeDao = database.crimeDao()
    private val executor =
        Executors.newSingleThreadExecutor()

    fun getPhotoFile(crime: Crime): File{
        return File(filesDir,crime.photoFileName)

    }

    fun getCrimes(): LiveData<List<Crime>> = crimeDao.getCrimes()


    fun getCrime(id: UUID): LiveData<Crime?> = crimeDao.getCrime(id)

    fun addCrime(crime: Crime){
        executor.execute{
            crimeDao.addCrime(crime)
        }
    }

    fun updateCrime(crime: Crime){
        executor.execute{
            crimeDao.updateCrime(crime)
        }
    }


    companion object {
        private var INSTANCE: CrimeRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get(): CrimeRepository =
            INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")


    }
}