package com.example.mcgrawtimeclock.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mcgrawtimeclock.dao.TransDAO
import com.example.mcgrawtimeclock.entity.TransEntity

@Database(entities = [TransEntity::class], version = 2, exportSchema = false)
abstract class TransDatabase : RoomDatabase() {
    abstract fun transDao(): TransDAO
}