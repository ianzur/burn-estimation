package com.example.burnestimation

import android.app.Application
import com.example.burnestimation.datamodel.PatientRepository
import com.example.burnestimation.datamodel.PatientRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

/**
 * Create a database instance and a repository instance when they are needed.
 *
 * Notes: (from androids: room-with-a-view tutorial)
 * You want to have only one instance of the database and of the repository in your app.
 * An easy way to achieve this is by creating them as members of the Application class.
 * Then they will just be retrieved from the Application whenever they're needed,
 * rather than constructed every time.
 */
class PatientsApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only create when they're needed
    // rather than when the application starts
    val database by lazy { PatientRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { PatientRepository(database.patientDao()) }
}