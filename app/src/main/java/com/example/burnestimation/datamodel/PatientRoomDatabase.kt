package com.example.burnestimation.datamodel

import android.content.ContentResolver
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.burnestimation.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import android.net.Uri


@Database(entities = [Patient::class], version = 1, exportSchema = false)
public abstract class PatientRoomDatabase: RoomDatabase() {

    abstract fun patientDao(): PatientDao

    private class PatientDatabaseCallback(
        private val scope: CoroutineScope,
        val context: Context,
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.patientDao(), context)
                }
            }
        }

        suspend fun populateDatabase(patientDao: PatientDao, context: Context) {
            // delete all content
            patientDao.deleteAll()

            // burn patients shipped with the app
            patientDao.insert(
                Patient(
                    null,
                    "D5GF5EJKQ30G",
                    "--",
                    70.0f * 2.54f,
                    150.0f / 2.205f,
                    "11/23/21",
                    "Dr. Bunsen Honeydew",
                    "Victorian Adult Burns Service",
                    R.drawable.vicburns_36.toString()
                )
            )

            patientDao.insert(
                Patient(
                    null,
                    "FJ0Y2S7HLQ42",
                    "--",
                    67.0f * 2.54f,
                    150.0f / 2.205f,
                    "11/23/21",
                    "Dr. Bunsen Honeydew",
                    "Victorian Adult Burns Service",
                    R.drawable.vicburns_16.toString()
                )
            )

            patientDao.insert(
                Patient(
                    null,
                    "A5DR863FL4E3",
                    "--",
                    24.0f * 2.54f,
                    25f / 2.205f,
                    "11/23/21",
                    "Dr. Bunsen Honeydew",
                    "unknown",
                    R.drawable.burned_baby.toString()
                )
            )
        }
    }

    companion object {
        // Singleton prevent multiple instance of database opening at the same time
        @Volatile
        private var INSTANCE: PatientRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): PatientRoomDatabase {
            // if the INSTANCE is not null, then return it
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PatientRoomDatabase::class.java,
                    "patient_database"
                )
                    .addCallback(PatientDatabaseCallback(scope, context))
                    .build()

                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}