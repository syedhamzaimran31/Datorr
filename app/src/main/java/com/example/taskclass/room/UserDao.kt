package com.example.taskclass.room

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface UserDao {
    @Insert
    fun insertMaleData(MaleData: MaleActivityData);

    @Insert
    fun insertFemaleData(FemaleData: FemaleActivityData);

}