package com.example.Datorr.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Login")
data class Login(
    @PrimaryKey(autoGenerate = true)
    val userId:Long?=null,
    val email:String?,
    val password:String?
)
