package com.example.taskclass.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    fun insertMaleData(MaleData: MaleActivityData);

    @Insert
    fun insertFemaleData(FemaleData: FemaleActivityData);
    @Query("SELECT * FROM maleactivitydata")
    fun getAllMales(): List<MaleActivityData>;

    @Query("SELECT * FROM femaleactivitydata")
    fun getAllFemales(): List<FemaleActivityData>
}