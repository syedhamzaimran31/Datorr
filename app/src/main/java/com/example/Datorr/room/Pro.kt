package com.example.Datorr.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "Pro")
data class Pro(
    @PrimaryKey(autoGenerate = true)
    val userId:Long?=null,
    val DOB: String?,
    val autoText:String,
    val image: String
)
