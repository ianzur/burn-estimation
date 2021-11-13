package com.example.burnestimation.datamodel

//import android.graphics.Bitmap
//import android.media.Image
import androidx.annotation.DrawableRes

/**
 * These data associated with an individual patient
 */
data class Patient(
    val id: String,
    val name: String,
    val height: Int,
    val weight: Int,
    val date: String,
    val physician: String,
    val institution: String,
    @DrawableRes val imageResourceId: Int
)