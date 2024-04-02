package com.example.Datorr.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Advance")
data class Advance(
    @PrimaryKey(autoGenerate = true)
    val userId: Long? = null,
    val image_1: String?,
    val image_2: String?,
    val image_3: String?,
    val image_4: String?,
    val uploadPDF: String?,
    val passingYear: String?,
)
