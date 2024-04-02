package com.example.Datorr.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MaleActivityData")
data class MaleActivityData(
    @PrimaryKey(autoGenerate = true)
    val userID: Long? = null,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val password: String?,
    val isAbove_18: Boolean?,
    val ageBirth:String?,
    val location_room:String?,
    val photoRoom:String?

)
