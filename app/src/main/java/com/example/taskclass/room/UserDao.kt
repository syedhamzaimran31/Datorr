package com.example.taskclass.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun insertMaleData(MaleData: MaleActivityData);

    @Insert
    suspend fun insertFemaleData(FemaleData: FemaleActivityData);

    @Insert
    suspend fun insertBasic(BasicData: Basic)

    @Insert
    suspend fun insertAdvance(AdvanceData: Advance)

    @Insert
    suspend fun insertPro(ProData: Pro)

    @Query("SELECT * FROM MaleActivityData")
    suspend fun getAllMales(): List<MaleActivityData>;

    @Query("SELECT * FROM FemaleActivityData")
    suspend fun getAllFemales(): List<FemaleActivityData>
}