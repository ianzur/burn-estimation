package com.example.burnestimation

import android.app.Application
import com.example.burnestimation.datamodel.PatientRepository
import com.example.burnestimation.datamodel.PatientRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class PatientsApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only create when they're needed
    // rather than when the application starts
    val database by lazy { PatientRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { PatientRepository(database.patientDao()) }
}