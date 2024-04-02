package com.example.Datorr.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Insert
    suspend fun insertLoginData(LogInData: Login)
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

    @Query("SELECT * FROM Basic")
    suspend fun getAllBasic(): List<Basic>

    @Query("SELECT * FROM Advance")
    suspend fun getAllAdvance(): List<Advance>

    @Query("SELECT * FROM Pro")
    suspend fun getAllPro(): List<Pro>

}