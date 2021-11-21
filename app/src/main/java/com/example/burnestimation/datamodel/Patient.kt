package com.example.burnestimation.datamodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import kotlin.math.log10
import kotlin.math.pow

/**
 * The data associated with an individual patient
 */
@Entity(tableName = "patient_table")
class Patient(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int? = null,
    @ColumnInfo(name = "hospitalId") var hospitalId: String = "",
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "height") var height: Float = 0.0f, //TODO: convert from imperial to metric
    @ColumnInfo(name = "weight") var weight: Float = 0.0f, //TODO: convert from imperial to metric
    @ColumnInfo(name = "date") var date: String = "", // TODO: convert to standard date time format
    @ColumnInfo(name = "attendingProvider") var attendingProvider: String = "",
    @ColumnInfo(name = "institution") var institution: String = "",
    @ColumnInfo(name = "imageUri") var imageUri: String = "",
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Patient

        if (id != other.id) return false
        if (name != other.name) return false
        if (height != other.height) return false
        if (weight != other.weight) return false
        if (date != other.date) return false
        if (attendingProvider != other.attendingProvider) return false
        if (institution != other.institution) return false
        if (imageUri != other.imageUri) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + attendingProvider.hashCode()
        result = 31 * result + institution.hashCode()
        return result
    }

    fun bodySurfaceArea(method: String="dubois"): Float {

        val h = height.toDouble()
        val w = weight.toDouble()

        val bsa = when (method) {
            "dubois" -> 0.007184 * w.pow(0.425) * h.pow(0.725)
            "mosteller" -> 0.016667 * w.pow(0.5) * h.pow(0.5)
            "haycock" -> 0.024265 * w.pow(0.5378) * h.pow(0.3964)
            "gehan_george" -> 0.0235 * w.pow(0.51456) * h.pow(0.42246)
            "boyd" -> 0.03330 * w.pow(0.6157 - 0.0188 * log10(w)) * h.pow(0.3)
            "fujimoto" -> 0.008883 * w.pow(0.444) * h.pow(0.663)
            "takahira" -> 0.007241 * w.pow(0.425) * h.pow(0.725)
            "schlich_male" -> 0.000975482 * w.pow(0.46) * h.pow(1.08)
            "schlich_female" -> 0.000579479 * w.pow(0.38) * h.pow(1.24)
            else -> -1.0
        }
        return bsa.toFloat()
    }
}