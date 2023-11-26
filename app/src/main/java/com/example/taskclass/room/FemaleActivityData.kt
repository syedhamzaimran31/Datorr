package com.example.taskclass.room

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
@Entity
data class FemaleActivityData(
    @PrimaryKey(autoGenerate = true)
    val userID: Int,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val password: String?
) {

  @Ignore
  constructor(firstName: String?, lastName: String?, email: String?, password: String?) :
            this(0, firstName, lastName, email, password)
}