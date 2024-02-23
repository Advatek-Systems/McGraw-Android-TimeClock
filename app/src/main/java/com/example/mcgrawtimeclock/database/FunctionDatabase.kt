package com.example.mcgrawtimeclock.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mcgrawtimeclock.dao.FunctionDAO
import com.example.mcgrawtimeclock.entity.FunctionEntity

@Database(entities = [FunctionEntity::class], version = 1, exportSchema = false)
abstract class FunctionDatabase : RoomDatabase() {
    abstract fun functionDao(): FunctionDAO
}