package com.example.taskclass.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Basic")
data class Basic(
    @PrimaryKey(autoGenerate = true)
    val userId: Long? = null,
    val cnicFront: String?,
    val cnicBack: String?,
    val cnicNumber: String?,
    val phoneNumber: String?,
    val selectPDF: String?
)
