package com.example.burnestimation.datamodel

import android.content.ContentResolver
import android.content.Context
import android.content.res.AssetManager
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.burnestimation.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import android.net.Uri
import java.io.File
import java.io.IOException


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
                    getCacheFile(context, "vicburns_36.jpg").absolutePath
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
                    getCacheFile(context, "vicburns_16.jpg").absolutePath
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
                    getCacheFile(context, "burned_baby.png").absolutePath
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

        // https://stackoverflow.com/a/19765960/8615419
        @Throws(IOException::class)
        fun getCacheFile(context: Context, filename: String): File = File(context.cacheDir, filename)
            .also {
                it.outputStream().use { cache -> context.assets.open(filename).use { it.copyTo(cache) } }
            }
    }
}