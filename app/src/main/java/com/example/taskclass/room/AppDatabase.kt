package com.example.taskclass.room

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [MaleActivityData::class,FemaleActivityData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun UserDao(): UserDao
}

