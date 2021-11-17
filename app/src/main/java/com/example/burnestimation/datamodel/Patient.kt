package com.example.burnestimation.datamodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * The data associated with an individual patient
 */
@Entity(tableName = "patient_table")
class Patient(
    @PrimaryKey @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "height") var height: Int = 0,
    @ColumnInfo(name = "weight") var weight: Int = 0,
    @ColumnInfo(name = "date") var date: String = "",
    @ColumnInfo(name = "attendingProvider") var attendingProvider: String = "",
    @ColumnInfo(name = "institution") var institution: String = "",
    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB) var image: ByteArray? = null
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
        if (!image.contentEquals(other.image)) return false

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
}