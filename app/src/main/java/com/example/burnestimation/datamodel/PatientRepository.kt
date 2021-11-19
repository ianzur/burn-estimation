package com.example.burnestimation.datamodel


import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow


// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class PatientRepository(private val patientDao: PatientDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allPatients: Flow<List<Patient>> = patientDao.getDateOrderedPatients()

    // By default Room runs suspend queries off the main thread, therefore, we don't need
    // to implement anything else to ensure we're not doing long running database work
    // off the main thread
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(patient: Patient) {
        patientDao.insert(patient)
    }

    // get single patient
    fun getPatient(patientId: Int): Flow<Patient> {
        return patientDao.getPatient(patientId)
    }

}
