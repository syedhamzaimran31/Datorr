package com.example.Datorr.room

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
@Entity(tableName = "FemaleActivityData")
data class FemaleActivityData(
    @PrimaryKey(autoGenerate = true)
    val userID: Int,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val password: String?,
    val isAbove_18:String?,
    val ageBirth:String?,
    val location_room:String?,
    val photoRoom:String?

) {

  @Ignore
  constructor(firstName: String?, lastName: String?, email: String?, password: String?,isAbove_18: String?,ageBirth: String?,location_room: String?,photoRoom: String?) :
            this(0, firstName, lastName, email, password,isAbove_18,ageBirth,location_room,photoRoom)
}