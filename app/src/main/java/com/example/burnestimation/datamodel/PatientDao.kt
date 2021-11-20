package com.example.burnestimation.datamodel

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {

    // return all patients ordered by date
    @Query("SELECT * FROM patient_table ORDER BY date DESC")
    fun getDateOrderedPatients(): Flow<List<Patient>>

    @Query("SELECT * FROM patient_table WHERE id = :patientdbId")
    fun getPatient(patientdbId: Int): Flow<Patient>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(patient: Patient): Long

    @Query("DELETE FROM patient_table")
    suspend fun deleteAll()

}