package com.example.burnestimation.datamodel

import android.R.attr
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory.decodeResource
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.burnestimation.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import android.graphics.drawable.BitmapDrawable
import java.io.ByteArrayOutputStream


@Database(entities = [Patient::class], version = 1, exportSchema = false)
public abstract class PatientRoomDatabase: RoomDatabase() {

    abstract fun patientDao(): PatientDao

    private class PatientDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.patientDao())
                }
            }
        }

        suspend fun populateDatabase(patientDao: PatientDao) {
            // delete all content
            patientDao.deleteAll()

            patientDao.insert(Patient("asdfoiuyh"))

        }
//
//        private fun resourceToBytes(id: Int) : ByteArray {
//
//            val bitmap = decodeResource(Resources.getSystem(), id)
//            val stream = ByteArrayOutputStream()
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
//            return stream.toByteArray()
//        }
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
                    "word_database"
                )
                    .addCallback(PatientDatabaseCallback(scope))
                    .build()

                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}